package org.mokey.acupple.jetcd;

import java.net.URI;

public class RedirectEtcdResponse extends EtcdResponse {
	URI location;

	public RedirectEtcdResponse() {
		super.setAction("REDIRECT");
		super.setResponseCode(307);
		super.setError(null);
	}

	public URI getLocation() {
		return location;
	}

	public void setLocation(URI location) {
		this.location = location;
	}

	public boolean successful() {
		return false;
	}

}
