package org.eclipse.dltk.ruby.ui.text;

import org.eclipse.dltk.ruby.core.IRubyConstants;
import org.eclipse.jface.text.IDocument;

public final class RubyPartitions {

	public static final String RUBY_PARTITIONING = IRubyConstants.RUBY_PARTITIONING;

	public static final String RUBY_COMMENT = "__ruby_comment";
	public static final String RUBY_STRING = "__ruby_string";
	public static final String RUBY_DOC = "__ruby_doc";

	public final static String[] RUBY_PARTITION_TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE, RubyPartitions.RUBY_STRING,
			RubyPartitions.RUBY_COMMENT, RubyPartitions.RUBY_DOC };

	private RubyPartitions() {

	}
}