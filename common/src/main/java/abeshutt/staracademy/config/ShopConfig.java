package abeshutt.staracademy.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

import java.util.*;

public class ShopConfig extends FileConfig {

    @Expose private Map<String, List<JsonElement>> offers;

    @Override
    public String getPath() {
        return "shop";
    }

    @Override
    protected void reset() {
        this.offers = new LinkedHashMap<>();

        this.offers.put("default", Arrays.asList(
                JsonParser.parseString("{'sell': {'id': 'minecraft:apple', 'count': 5}, 'price': 100}".replace("'", "\""))
        ));
    }

}
