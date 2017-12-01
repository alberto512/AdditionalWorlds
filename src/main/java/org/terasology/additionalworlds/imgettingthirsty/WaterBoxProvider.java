/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.additionalworlds.imgettingthirsty;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(WaterBoxFacet.class)
@Requires(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 8)))
public class WaterBoxProvider implements FacetProvider {

    private SimplexNoise noise;

    @Override
    public void setSeed(long seed) {
        noise = new SimplexNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(WaterBoxFacet.class).extendBy(0, 8, 4);
        WaterBoxFacet facet = new WaterBoxFacet(region.getRegion(), border);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        for (BaseVector2i position : surfaceHeightFacet.getWorldRegion().contents()) {
            int surfaceHeight = (int) surfaceHeightFacet.getWorld(position);

            if (facet.getWorldRegion().encompasses(position.getX(), surfaceHeight, position.getY())
                    && noise.noise(position.getX(), position.getY()) > 0.99) {
                facet.setWorld(position.getX(), surfaceHeight, position.getY(), new WaterBox());
            }
        }

        region.setRegionFacet(WaterBoxFacet.class, facet);
    }
}
