package graph_encryption.service;

import graph_encryption.util.EncryptLSB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EncryptService {

    public final EncryptLSB encryptLSB;

    @Autowired
    public EncryptService(EncryptLSB encryptLSB) {
        this.encryptLSB = encryptLSB;
    }

    public void encrypt(File imageFile, String message){
        encryptLSB.Encrypt(imageFile,message);
    }
}
