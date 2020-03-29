package samples;

import samples.camera.Camera;

/**
 * All Sample gaame will implements this interface, just to simplify
 * implmentation between all example thoorugh inheritance.
 */
public interface Sample {
    public int getWidth();
    public int getHeight();
    public double getScale();
    public String getTitle();
    public Camera getActiveCamera();

    public void initialize();
    public void loop();
    public void update(double elapsed);
    public void render(long realFps);
}