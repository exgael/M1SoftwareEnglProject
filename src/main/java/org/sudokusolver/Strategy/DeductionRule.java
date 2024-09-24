package org.sudokusolver.Strategy;

import org.sudokusolver.Strategy.Regions.RegionManager;

@FunctionalInterface
public interface DeductionRule {
    boolean apply(RegionManager regionManager);
}
