package pt.up.hs.sampling.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Files {
    public static byte[] listBytesToZip(Map<String, byte[]> filesMap) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        for (Map.Entry<String, byte[]> file : filesMap.entrySet()) {
            ZipEntry entry = new ZipEntry(file.getKey());
            entry.setSize(file.getValue().length);
            zos.putNextEntry(entry);
            zos.write(file.getValue());
        }
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }
}
