package org.terasology.desertworld;

import org.terasology.math.Region3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.BaseFieldFacet3D;

public class LakeFacet extends BaseFieldFacet3D {
    public LakeFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}
