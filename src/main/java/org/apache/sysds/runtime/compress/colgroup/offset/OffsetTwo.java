/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.sysds.runtime.compress.colgroup.offset;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OffsetTwo extends AOffset {

	private final int first;
	private final int last;

	public OffsetTwo(int first, int last) {
		this.first = first;
		this.last = last;
	}

	@Override
	public AIterator getIterator() {
		return new IterateTwo();
	}

	@Override
	public AOffsetIterator getOffsetIterator() {
		return new IterateOffsetTwo();
	}

	@Override
	public long getExactSizeOnDisk() {
		return 1 + 4 + 4;
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getOffsetToFirst() {
		return first;
	}

	@Override
	public int getOffsetToLast() {
		return last;
	}

	@Override
	public int getOffsetsLength() {
		return 1;
	}

	@Override
	public long getInMemorySize() {
		return estimateInMemorySize();
	}

	public static long estimateInMemorySize() {
		return 16 + 4 + 4; // object header plus int
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeByte(OffsetFactory.OFF_TYPE.TWO_OFFSET.ordinal());
		out.writeInt(first);
		out.writeInt(last);
	}

	public static OffsetTwo readFields(DataInput in) throws IOException {
		return new OffsetTwo(in.readInt(), in.readInt());
	}

	private class IterateTwo extends AIterator {

		private IterateTwo() {
			super(first);
		}

		@Override
		public int next() {
			offset = last;
			return last;
		}

		@Override
		public int skipTo(int idx) {
			if(idx > first ){
				offset = last;
				return last;
			}
			return first;
		}

		@Override
		public IterateTwo clone() {
			IterateTwo ret = new IterateTwo();
			ret.offset = offset;
			return ret;
		}

		@Override
		public int getDataIndex() {
			return offset == first ? 0 : 1;
		}

		@Override
		public int getOffsetsIndex() {
			return offset == first ? 0 : 1;
		}
	}

	private class IterateOffsetTwo extends AOffsetIterator {

		private IterateOffsetTwo() {
			super(first);
		}

		@Override
		public int next() {
			return offset = last;
		}
	}
}
