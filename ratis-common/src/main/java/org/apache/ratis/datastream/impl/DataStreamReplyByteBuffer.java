/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ratis.datastream.impl;

import org.apache.ratis.protocol.DataStreamPacket;
import org.apache.ratis.protocol.DataStreamReply;
import org.apache.ratis.protocol.DataStreamReplyHeader;
import org.apache.ratis.proto.RaftProtos.DataStreamPacketHeaderProto.Type;

import java.nio.ByteBuffer;

/**
 * Implements {@link DataStreamReply} with {@link ByteBuffer}.
 *
 * This class is immutable.
 */
public final class DataStreamReplyByteBuffer extends DataStreamPacketByteBuffer implements DataStreamReply {
  public static final class Builder {
    private Type type;
    private long streamId;
    private long streamOffset;
    private ByteBuffer buffer;

    private boolean success;
    private long bytesWritten;

    private Builder() {}

    public Builder setType(Type type) {
      this.type = type;
      return this;
    }

    public Builder setStreamId(long streamId) {
      this.streamId = streamId;
      return this;
    }

    public Builder setStreamOffset(long streamOffset) {
      this.streamOffset = streamOffset;
      return this;
    }

    public Builder setBuffer(ByteBuffer buffer) {
      this.buffer = buffer;
      return this;
    }

    public Builder setSuccess(boolean success) {
      this.success = success;
      return this;
    }

    public Builder setBytesWritten(long bytesWritten) {
      this.bytesWritten = bytesWritten;
      return this;
    }

    public Builder setDataStreamReplyHeader(DataStreamReplyHeader header) {
      return setDataStreamPacket(header)
          .setSuccess(header.isSuccess())
          .setBytesWritten(header.getBytesWritten());
    }

    public Builder setDataStreamPacket(DataStreamPacket packet) {
      return setType(packet.getType())
          .setStreamId(packet.getStreamId())
          .setStreamOffset(packet.getStreamOffset());
    }

    public DataStreamReplyByteBuffer build() {
      return new DataStreamReplyByteBuffer(type, streamId, streamOffset, buffer, success, bytesWritten);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private final boolean success;
  private final long bytesWritten;

  private DataStreamReplyByteBuffer(Type type, long streamId, long streamOffset, ByteBuffer buffer,
      boolean success, long bytesWritten) {
    super(type, streamId, streamOffset, buffer);

    this.success = success;
    this.bytesWritten = bytesWritten;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }

  @Override
  public long getBytesWritten() {
    return bytesWritten;
  }

  @Override
  public String toString() {
    return super.toString()
        + "," + (success? "SUCCESS": "FAILED")
        + ",bytesWritten=" + bytesWritten;
  }
}
