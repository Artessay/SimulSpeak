package org.simulspeak.bridge.component;

import org.junit.jupiter.api.Test;
import org.simulspeak.bridge.configuration.BridgeConfig;

public class StorageImplementTest {

    private StorageImplement storageImplement = new StorageImplement();

    @Test
    void testApply() {
        String image = storageImplement.apply(2L, 1L, BridgeConfig.APPLY_IMAGE_ENUM);
        System.out.println(image);
    }

    @Test
    void testRecommend() {
        storageImplement.recommend();
    }

    @Test
    void testRecommendList() {

    }

    @Test
    void testRequestVideo() {

    }

    @Test
    void testUpload() {

    }

    @Test
    void testUploadVideo() {

    }
}
