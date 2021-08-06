package gradingTools.shared.testcases.concurrency.propertyChanges;

public interface Selector<SelectedType> {
  boolean selects(SelectedType anObject);
}
