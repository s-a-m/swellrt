package org.swellrt.model;

import java.util.Set;

import org.waveprotocol.wave.model.id.WaveId;
import org.waveprotocol.wave.model.id.WaveletId;
import org.waveprotocol.wave.model.wave.ParticipantId;

public interface ReadableModel {

  public WaveId getWaveId();

  public WaveletId getWaveletId();

  public Set<ParticipantId> getParticipants();

  public ReadableMap getRoot();

  public ReadableType fromPath(String path);

}
