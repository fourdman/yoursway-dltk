package org.eclipse.dltk.dbgp.internal;

import java.net.URI;

import org.eclipse.dltk.dbgp.IDbgpSessionInfo;

public class DbgpSessionInfo implements IDbgpSessionInfo {
	private String appId;

	private String ideKey;

	private String session;

	private String threadId;

	private String parentId;

	private String language;

	private URI fileUri;

	public DbgpSessionInfo(String appId, String ideKey, String session,
			String threadId, String parentId, String language, URI fileUri) {
		super();
		this.appId = appId;
		this.ideKey = ideKey;
		this.session = session;
		this.threadId = threadId;
		this.parentId = parentId;
		this.language = language;
		this.fileUri = fileUri;
	}

	public String getApplicationId() {
		return appId;
	}

	public URI getFileUri() {
		return fileUri;
	}

	public String getIdeKey() {
		return ideKey;
	}

	public String getLanguage() {
		return language;
	}

	public String getParentAppId() {
		return parentId;
	}

	public String getSession() {
		return session;
	}

	public String getThreadId() {
		return threadId;
	}

}
