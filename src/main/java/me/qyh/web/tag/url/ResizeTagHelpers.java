package me.qyh.web.tag.url;

import me.qyh.utils.Files;
import me.qyh.web.Webs;

public final class ResizeTagHelpers {
	
	public static String getUrl(String url,Integer size){
		String _url = null;
		if (size == null) {
			_url = url;
		} else {
			if (Webs.isWebImage(url)) {
				_url = (Files.appendFilename(url, "_", size));
			} else {
				_url = url;
			}
		}
		return _url;
	}

}
