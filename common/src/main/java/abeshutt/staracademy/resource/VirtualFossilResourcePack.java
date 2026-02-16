package abeshutt.staracademy.resource;

import abeshutt.staracademy.StarAcademyMod;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.AbstractFileResourcePack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackInfo;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VirtualFossilResourcePack implements ResourcePack {

  private final ResourcePackInfo info;
  private final Map<String, String> data = new HashMap<>();

  public VirtualFossilResourcePack(ResourcePackInfo info) {
    this.info = info;

    // Fossil Definitions
    data.put("fossils/ha_fossil.json", """
        {
          "result": "random_common ability=hidden",
          "fossils": [
            "academy:ha_fossil"
          ]
        }
        """);

    data.put("fossils/shiny_fossil.json", """
        {
          "result": "random_common shiny=yes",
          "fossils": [
            "academy:shiny_fossil"
          ]
        }
        """);

    data.put("fossils/max_iv_fossil.json",
        """
            {
              "result": "random_common hp_iv=31 attack_iv=31 defence_iv=31 special_attack_iv=31 special_defence_iv=31 speed_iv=31",
              "fossils": [
                "academy:max_iv_fossil"
              ]
            }
            """);

    data.put("fossils/radiant_fossil.json", """
        {
          "result": "random_common shiny=yes aspect=radiant-radiant",
          "fossils": [
            "academy:radiant_fossil"
          ]
        }
        """);

    data.put("tags/items/fossils.json", """
        {
          "replace": false,
          "values": [
            "academy:ha_fossil",
            "academy:max_iv_fossil",
            "academy:radiant_fossil",
            "academy:shiny_fossil"
          ]
        }
        """);

    data.put("textures/gui/summary/aspects/radiant.png", "assets/academy/textures/gui/summary/icon_radiant.png");
    data.put("textures/gui/summary/aspects/radiant_radiant.png",
        "assets/academy/textures/gui/summary/icon_radiant.png");
    data.put("textures/gui/summary/aspects/radiant-radiant.png",
        "assets/academy/textures/gui/summary/icon_radiant.png");
  }

  @Nullable
  @Override
  public InputSupplier<InputStream> openRoot(String... segments) {
    return null;
  }

  @Nullable
  @Override
  public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
    if (type == ResourceType.SERVER_DATA && id.getNamespace().equals("cobblemon")) {
      String content = data.get(id.getPath());
      if (content != null) {
        StarAcademyMod.LOGGER.info("Opening Virtual Resource: " + id.getPath());
        return () -> new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
      }
    }
    if (type == ResourceType.CLIENT_RESOURCES && id.getNamespace().equals("cobblemon")) {
      String path = data.get(id.getPath());
      if (path != null) {
        return () -> StarAcademyMod.class.getClassLoader().getResourceAsStream(path);
      }
    }
    return null;
  }

  @Override
  public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
    if (type == ResourceType.SERVER_DATA && namespace.equals("cobblemon")) {
      for (String path : data.keySet()) {
        if (path.startsWith(prefix)) {
          StarAcademyMod.LOGGER.info("Found Virtual Resource: " + path);
          consumer.accept(new Identifier("cobblemon", path), open(type, new Identifier("cobblemon", path)));
        }
      }
    }
    if (type == ResourceType.CLIENT_RESOURCES && namespace.equals("cobblemon")) {
      for (String path : data.keySet()) {
        if (path.startsWith(prefix)) {
          consumer.accept(new Identifier("cobblemon", path), open(type, new Identifier("cobblemon", path)));
        }
      }
    }
  }

  @Override
  public Set<String> getNamespaces(ResourceType type) {
    return type == ResourceType.SERVER_DATA || type == ResourceType.CLIENT_RESOURCES ? Set.of("cobblemon")
        : Collections.emptySet();
  }

  @Nullable
  @Override
  public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) {
    String json = """
        {
          "pack": {
            "pack_format": 15,
            "description": "Cobble Academy Fossils"
          }
        }
        """;
    try (InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
      return AbstractFileResourcePack.parseMetadata(metaReader, stream);
    } catch (Exception e) {
      StarAcademyMod.LOGGER.error("Failed to parse virtual pack metadata", e);
      return null;
    }
  }

  @Override
  public ResourcePackInfo getInfo() {
    return this.info;
  }

  @Override
  public void close() {

  }

}
