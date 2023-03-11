package dk.sebsa;

import dk.sebsa.spellbook.asset.loading.AssetProvider;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebsn
 * @since 0.0.1
 */
@Builder
public class SpellbookCapabilities {
    @Builder.Default public final boolean spellbookDebug = true;
    @Getter private final List<AssetProvider> assetsProviders = new ArrayList<>();

    public SpellbookCapabilities addAssetProvider(AssetProvider provider) {
        this.assetsProviders.add(provider);
        return this;
    }
}
