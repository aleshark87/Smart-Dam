package controller;

/**
 * Listener to implement communication between serialVerticle and Model
 * @author aless
 *
 */
public interface ManualModeListener {

        public void manualMode(final boolean set);
        
        public void manualDamOpening(final int damOpening);
}
