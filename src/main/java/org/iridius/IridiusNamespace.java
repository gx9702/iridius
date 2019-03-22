/*
 * Copyright (c) 2016 Kevin Herron
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.html.
 */
package org.iridius;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.AccessContext;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.MethodInvocationHandler;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.api.Namespace;
import org.eclipse.milo.opcua.sdk.server.nodes.AttributeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.NodeFactory;
import org.eclipse.milo.opcua.sdk.server.nodes.ServerNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.eclipse.milo.opcua.stack.core.util.FutureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;

public class IridiusNamespace implements Namespace {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final SubscriptionModel subscriptionModel;


    private final OpcUaServer server;
    private final UShort namespaceIndex;

    UaFolderNode rootNode;
    String name;
    String uri;
    Map config;
    
    Map<UaNode, DeviceTag> nodesToTags = new HashMap<>();
    
    // Keeps track of all added devices (maybe it is not useful).
    Set<Device> devices = new HashSet();

    public IridiusNamespace(OpcUaServer server, UShort namespaceIndex, Map config) {
        this.server = server;
        this.namespaceIndex = namespaceIndex;
        this.name = (String) config.get("name");
        this.uri = (String) config.get("uri");
        this.config = config;

        subscriptionModel = new SubscriptionModel(server, this);


        try {
            // Create a "HelloWorld" folder and add it to the node manager
            NodeId folderNodeId = new NodeId(namespaceIndex, name);

            rootNode = new UaFolderNode(
                    server.getNodeMap(),
                    folderNodeId,
                    new QualifiedName(namespaceIndex, name),
                    LocalizedText.english(name)
            );

            server.getNodeMap().addNode(rootNode);

            // Make sure our new folder shows up under the server's Objects folder
            server.getUaNamespace().addReference(
                    Identifiers.ObjectsFolder,
                    Identifiers.Organizes,
                    true,
                    folderNodeId.expanded(),
                    NodeClass.Object
            );

        } catch (UaException e) {
            logger.error("Error adding nodes: {}", e.getMessage(), e);
        }
    }

    /**
     * Write something.
     * 
     * @param device
     */
    public void addDevice(Device device) {
        
        // Just a container of all added devices
        devices.add(device);
        
        // Create a folder for each device added
        UaFolderNode folder = new UaFolderNode(
                server.getNodeMap(),
                new NodeId(namespaceIndex, name + "/" + device.getName()),
                new QualifiedName(namespaceIndex, device.getName()),
                LocalizedText.english(device.getName())
        );
       

        server.getNodeMap().addNode(folder);
        rootNode.addOrganizes(folder);
        
        // Get all tags from the device and create a corresponding OPC-UA node. Link them.
        // The node is then added to the device folder created above.

        for (DeviceTag tag : device.getTags()) {

            // Create a node for each tag. Tag should probably provide more node properties, for example the
            // historicizing property.
            UaVariableNode node = new UaVariableNode.UaVariableNodeBuilder(server.getNodeMap())
                    .setNodeId(new NodeId(namespaceIndex, this.name + "/" + device.getName() + "/" + tag.getName()))
                    .setAccessLevel(ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
                    .setUserAccessLevel(ubyte(AccessLevel.getMask(AccessLevel.READ_WRITE)))
                    .setBrowseName(new QualifiedName(namespaceIndex, tag.getName()))
                    .setDisplayName(LocalizedText.english(tag.getDisplayName()))
                    .setDataType(tag.getDataType())
                    .setTypeDefinition(Identifiers.BaseDataVariableType)
                    .setHistorizing(false)
                    .setValue(new DataValue(new Variant(""), StatusCode.UNCERTAIN))
                    .build();

            //node.setValue(new DataValue(Variant.NULL_VALUE));
            tag.setNode(node);
            
            nodesToTags.put(node, tag);

            folder.addOrganizes(node);
        }
    }

    @Override
    public UShort getNamespaceIndex() {
        return namespaceIndex;
    }

    @Override
    public String getNamespaceUri() {
        //return NAMESPACE_URI;
        return uri;
    }
    
    @Override
    public CompletableFuture<List<Reference>> browse(AccessContext context, NodeId nodeId) {
        ServerNode node = server.getNodeMap().get(nodeId);

        if (node != null) {
            return CompletableFuture.completedFuture(node.getReferences());
        } else {
            return FutureUtils.failedFuture(new UaException(StatusCodes.Bad_NodeIdUnknown));
        }
    }

    @Override
    public void read(
            ReadContext context,
            Double maxAge,
            TimestampsToReturn timestamps,
            List<ReadValueId> readValueIds) {

        List<DataValue> results = Lists.newArrayListWithCapacity(readValueIds.size());

        for (ReadValueId readValueId : readValueIds) {
            ServerNode node = server.getNodeMap().get(readValueId.getNodeId());

            if (node != null) {
                DeviceTag tag = nodesToTags.get(node);
                if (tag != null) {
                    
                }
                DataValue value = node.readAttribute(
                        new AttributeContext(context),
                        readValueId.getAttributeId(),
                        timestamps,
                        readValueId.getIndexRange(),
                        readValueId.getDataEncoding()
                );

                results.add(value);
            } else {
                results.add(new DataValue(StatusCodes.Bad_NodeIdUnknown));
            }
        }

        context.complete(results);
    }

    @Override
    public void write(WriteContext context, List<WriteValue> writeValues) {
        List<StatusCode> results = Lists.newArrayListWithCapacity(writeValues.size());

        for (WriteValue writeValue : writeValues) {
            ServerNode node = server.getNodeMap().get(writeValue.getNodeId());

            if (node != null) {
                System.out.println("Writing to node " + node.getNodeId());
                try {
                    node.writeAttribute(
                            new AttributeContext(context),
                            writeValue.getAttributeId(),
                            writeValue.getValue(),
                            writeValue.getIndexRange()
                    );
                    
                    // Find the device/tag connected to this node and
                    DeviceTag tag = nodesToTags.get(node);
                    if (tag != null) {
                        Device device = tag.getDevice();
                        device.write(tag, writeValue.getValue().getValue());
                        System.out.println("Writing to tag: " + tag.getName());
                    } else {
                        System.out.println("Tag not found");
                    }

                    results.add(StatusCode.GOOD);

                    logger.error(
                            "Wrote value {} to {} attribute of {}",
                            writeValue.getValue().getValue(),
                            AttributeId.from(writeValue.getAttributeId()).map(Object::toString).orElse("unknown"),
                            node.getNodeId());
                } catch (UaException e) {
                    logger.error("Unable to write value={}", writeValue.getValue(), e);
                    results.add(e.getStatusCode());
                }
            } else {
                results.add(new StatusCode(StatusCodes.Bad_NodeIdUnknown));
            }
        }

        context.complete(results);
    }

    @Override
    public void onDataItemsCreated(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsCreated(dataItems);
    }

    @Override
    public void onDataItemsModified(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsModified(dataItems);
    }

    @Override
    public void onDataItemsDeleted(List<DataItem> dataItems) {
        subscriptionModel.onDataItemsDeleted(dataItems);
    }

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
        subscriptionModel.onMonitoringModeChanged(monitoredItems);
    }

    @Override
    public Optional<MethodInvocationHandler> getInvocationHandler(NodeId methodId) {
        Optional<ServerNode> node = server.getNodeMap().getNode(methodId);

        return node.flatMap(n -> {
            if (n instanceof UaMethodNode) {
                return ((UaMethodNode) n).getInvocationHandler();
            } else {
                return Optional.empty();
            }
        });
    }

}
