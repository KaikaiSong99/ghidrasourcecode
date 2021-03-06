/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.util.bin.format.elf;

import static ghidra.app.util.bin.StructConverter.*;

import java.io.IOException;

import ghidra.app.util.bin.*;
import ghidra.docking.settings.SettingsImpl;
import ghidra.program.model.data.*;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.MemBuffer;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.util.NumericUtilities;
import ghidra.util.exception.DuplicateNameException;

/**
 * Factory data type that marks up a ELF .gnu_debuglink section.
 */
public class GnuDebugLinkSection extends FactoryStructureDataType {
	public static final String DEBUG_LINK_SECTION_NAME = ".gnu_debuglink";

	public static GnuDebugLinkSectionValues fromProgram(Program program) {
		MemoryBlock debugLinkSection = program.getMemory().getBlock(DEBUG_LINK_SECTION_NAME);
		if (debugLinkSection == null) {
			return null;
		}
		try (ByteProvider bp = MemoryByteProvider.createMemoryBlockByteProvider(program.getMemory(),
			debugLinkSection)) {
			BinaryReader br = new BinaryReader(bp, !program.getMemory().isBigEndian());
			String filename = br.readNextAsciiString();
			br.setPointerIndex(NumericUtilities.getUnsignedAlignedValue(br.getPointerIndex(), 4));
			int crc = br.readNextInt();
			return new GnuDebugLinkSectionValues(filename, crc);
		}
		catch (IOException e) {
			// fall thru and return null
		}
		return null;
	}

	public static class GnuDebugLinkSectionValues {
		private String filename;
		private int crc;

		public GnuDebugLinkSectionValues(String filename, int crc) {
			this.filename = filename;
			this.crc = crc;
		}

		public String getFilename() {
			return filename;
		}

		public int getCrc() {
			return crc;
		}
	}

	private long sectionSize;

	/**
	 * Creates a new GnuDebugLinkDataType instance.
	 * 
	 * @param dtm the program's {@link DataTypeManager}
	 * @param sectionSize the size of the section (for bounds checking)
	 */
	public GnuDebugLinkSection(DataTypeManager dtm, long sectionSize) {
		super("Gnu_DebugLink", dtm);
		this.sectionSize = sectionSize;
	}

	@Override
	public DataType clone(DataTypeManager dtm) {
		if (dtm == dataMgr) {
			return this;
		}
		return new GnuDebugLinkSection(dtm, sectionSize);
	}

	@Override
	protected void populateDynamicStructure(MemBuffer buf, Structure es) {
		StringDataInstance filenameStr = StringDataInstance.getStringDataInstance(
			StringDataType.dataType, buf, SettingsImpl.NO_SETTINGS, -1);
		int filenameLen = filenameStr.getStringLength();
		if (filenameLen <= 0 || filenameLen + 4 /* crc field */ > sectionSize) {
			return;
		}
		filenameLen = (int) NumericUtilities.getUnsignedAlignedValue(filenameLen, 4);
		es.add(StringDataType.dataType, filenameLen, "filename", "Debug file name");
		es.add(DWORD, "crc", null);
	}

	@Override
	protected Structure setCategoryPath(Structure struct, MemBuffer buf) {
		try {
			struct.setCategoryPath(new CategoryPath("/ELF"));
		}
		catch (DuplicateNameException e) {
			// ignore - will not happen
		}
		return struct;
	}

}
