package model;


public interface Sound
{
  float volume = 10;

  /**
   * Get the volume of this sound
   * @return volume
   */
  float getVolume ();

  /**
   * Set the volume of the sound.
   * This should be done very carefully as sharp volume changes are not ok.
   * @param volume volume to be set to
   */
  void setVolume (float volume);
}
