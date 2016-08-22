package com.company.data.model.proto;

import java.util.Base64;

/**
 * Created by Yevhen on 06.08.2016.
 */
public class ImageObject {
    protected String base64EncodeToString(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
