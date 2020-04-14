package de.mhus.lib.core.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.errors.MException;

public abstract class IConfigBuilder extends MLog {

    
    public abstract IConfig read(InputStream is) throws MException;
    
    public abstract void write(IConfig config, OutputStream os) throws MException;
    
    public IConfig readFromFile(File file) throws MException {
        try (FileInputStream is = new FileInputStream(file)) {
            return read(is);
        } catch (IOException e) {
            throw new MException(file, e);
        }
    }

    public IConfig readFromString(String content) throws MException {
        return read(new ByteArrayInputStream(MString.toBytes(content)));
    }
    
    public void writeToFile(IConfig config, File file) throws MException {
        try (FileOutputStream os = new FileOutputStream(file)) {
            write(config, os);
        } catch (IOException e) {
            throw new MException(file,e);
        }
    }
    
    public String writeToString(IConfig config) throws MException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        write(config, os);
        return MString.byteToString(os.toByteArray());
    }

}
