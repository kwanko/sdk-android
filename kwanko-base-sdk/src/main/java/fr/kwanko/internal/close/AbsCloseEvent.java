package fr.kwanko.internal.close;

/**
 * SourceCode
 * Created by erusu on 6/14/2017.
 */

abstract class AbsCloseEvent implements OnCloseListener.CloseEvent {

    protected abstract String getName();

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj instanceof AbsCloseEvent){
            return getName()!= null && getName().equals(((AbsCloseEvent) obj).getName());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getName()!=null?getName().hashCode():super.hashCode();
    }
}
