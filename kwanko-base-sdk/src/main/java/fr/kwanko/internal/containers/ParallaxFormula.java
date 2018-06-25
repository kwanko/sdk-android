package fr.kwanko.internal.containers;

/**
 * SourceCode
 * Created by erusu on 5/4/2017.
 */

class ParallaxFormula {

    private float dh;
    private float dH;
    private float yBig;
    private float iH;
    private float ih;
    private ComputationStrategy computationStrategy;

    void initConstants(float dh,float dH, float yBig,float iHbig){
        this.dh = dh;
        this.dH = dH;
        this.yBig = yBig;
        this.iH = iHbig;
        this.ih = ih(iHbig);
        this.computationStrategy = createComputationStrategy();
    }

    private ComputationStrategy createComputationStrategy(){
        if(dh >= dH){
            return new AdBiggerComputationStrategy();
        }else {
            return new AdSmallerComputationStrategy();
        }
    }

    final float ih(float iH){
        return iH;
    }

    float compute(int x){
        if(computationStrategy == null){
            throw new IllegalStateException("please call initConstants method first");
        }
        return computationStrategy.compute(x);
    }

    private interface ComputationStrategy{

        float compute(int x);
    }

    private class AdBiggerComputationStrategy implements ComputationStrategy{

        @Override
        public float compute(int x) {
            return k()*x - k() * (yBig+iH);
        }

        private float k(){
            return (dh + ih)/(-dH - iH) ;
        }
    }

    private class AdSmallerComputationStrategy implements ComputationStrategy{

        @Override
        public float compute(int x) {
            if(x >= yBig - dH && x<(yBig-dH)+ih){
                return dh + ih - (x-(yBig-dH));
            } else if(x>=(yBig-dH)+ih && x< yBig){
                return computeInterpolationFormula(x);
            } else {
                return ih - (x - yBig);
            }
        }

        private float computeInterpolationFormula(int x){
            return k()*x + ih - k() * yBig;
        }

        private float k(){
            return (dh - ih)/((yBig - dH) + ih - yBig);
        }
    }
}
