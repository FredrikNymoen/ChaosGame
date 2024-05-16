package gui;

public interface ChaosGameObserver {
  void onSliderValueChanged(int value);
  void onCanvasSizeChanged();
  void onTransformationSelected(String transformation);

}
