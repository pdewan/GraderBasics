package grader.basics.concurrency.propertyChanges;

public interface Selector<SelectedType> {
  boolean selects(SelectedType anObject);
}
