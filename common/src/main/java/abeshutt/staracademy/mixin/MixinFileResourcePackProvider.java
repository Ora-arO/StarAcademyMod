package abeshutt.staracademy.mixin;

import abeshutt.staracademy.StarAcademyMod;
import abeshutt.staracademy.block.entity.renderer.DynamicResourcePack;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.resource.ResourcePackProfile.InsertionPosition.TOP;

@Mixin(FileResourcePackProvider.class)
public abstract class MixinFileResourcePackProvider {

    @Shadow
    @Final
    private ResourceType type;
    @Shadow
    @Final
    private ResourcePackSource source;

    @Inject(method = "register", at = @At("RETURN"))
    public void register(Consumer<ResourcePackProfile> profileAdder, CallbackInfo ci) {
        if (this.type == ResourceType.SERVER_DATA) {
            StarAcademyMod.LOGGER.info("Enabling VirtualFossilResourcePack in MixinFileResourcePackProvider");
            ResourcePackProfile profile = ResourcePackProfile.create(
                    new ResourcePackInfo("virtual_fossils", Text.literal("Virtual Fossils"), this.source,
                            Optional.empty()),
                    new ResourcePackProfile.PackFactory() {
                        @Override
                        public ResourcePack open(ResourcePackInfo info) {
                            return new abeshutt.staracademy.resource.VirtualFossilResourcePack(info);
                        }

                        @Override
                        public ResourcePack openWithOverlays(ResourcePackInfo info,
                                ResourcePackProfile.Metadata metadata) {
                            return new abeshutt.staracademy.resource.VirtualFossilResourcePack(info);
                        }
                    },
                    ResourceType.SERVER_DATA,
                    new ResourcePackPosition(true, TOP, true));
            if (profile != null) {
                profileAdder.accept(profile);
            }
            return;
        }

        DynamicResourcePack.open(this.type, this.source, (path, packFactory, info) -> {
            ResourcePackProfile resourcePackProfile = ResourcePackProfile.create(info, packFactory, this.type,
                    new ResourcePackPosition(true, TOP, true));

            if (resourcePackProfile != null) {
                profileAdder.accept(resourcePackProfile);
            }
        });
    }

}
