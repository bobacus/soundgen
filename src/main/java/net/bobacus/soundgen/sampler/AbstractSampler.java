package net.bobacus.soundgen.sampler;


abstract class AbstractSampler implements Sampler {

    AbstractSampler(SamplerParams params) {
        this.params = params;
    }

    final SamplerParams params;

}
