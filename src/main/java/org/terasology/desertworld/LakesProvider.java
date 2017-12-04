package org.terasology.desertworld;

import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2f;
import org.terasology.math.geom.Vector3i;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.PerlinNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProviderPlugin;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(bottom = 1, sides = 11)))
@Produces(LakeFacet.class)
public class LakesProvider implements FacetProviderPlugin {

    private Noise lakeNoise;

    @Override
    public void setSeed(long seed) {
        lakeNoise = new SubSampledNoise(new BrownianNoise(new PerlinNoise(seed + 3), 4), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
        LakeFacet facet = new LakeFacet(region.getRegion(), border);
        float lakeDepth = 10;
        // loop through every position on our 2d array
        for (Vector3i position : facet.getWorldRegion()) {
            float additiveLakeDepth = lakeNoise.noise(position.x(), position.y()) * lakeDepth;
            // dont bother adding lake height,  that will allow unaffected regions
            additiveLakeDepth = TeraMath.clamp(additiveLakeDepth, -lakeDepth, 0);

            facet.setWorld(position, facet.getWorld(position) + additiveLakeDepth);
        }

        region.setRegionFacet(LakeFacet.class, facet);
    }
}
