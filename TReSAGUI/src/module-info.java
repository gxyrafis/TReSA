module TReSAGUI {
	requires javafx.controls;
	requires uberjar;
	requires lucene.queryparser;
	requires javafx.graphics;
	requires javafx.base;
	requires lucene.queries;
	opens application to javafx.graphics, javafx.fxml;
}
