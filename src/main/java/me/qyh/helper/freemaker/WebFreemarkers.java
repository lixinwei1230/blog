package me.qyh.helper.freemaker;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.cache.CacheStorage;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import me.qyh.exception.SystemException;
import me.qyh.helper.refresh.Refresh;
import me.qyh.utils.Validators;
import me.qyh.web.tag.url.ResizeTagHelpers;
import me.qyh.web.tag.url.UrlHelper;

/**
 * 用来解析信息,邮件模板
 * 
 * @author mhlx
 *
 */
public class WebFreemarkers implements Refresh {

	private static final String URL_HELPER = "urlHelper";
	private static final String RESIZE = "Resize";

	@Autowired
	private FreeMarkerConfigurer freeMarker;
	@Autowired
	private UrlHelper urlHelper;
	private Map<String, String> staticModels = new HashMap<String, String>();
	private Map<String, Object> publicDatas = new HashMap<String, Object>();

	/**
	 * 将freemarker模板解析为文本，需要在web环境中
	 * 
	 * @param temlatePath
	 *            模板相对路径 {@code FreeMarkerConfigurer}
	 * @param model
	 *            解析变量
	 * @return 解析后的文本
	 */
	public String processTemplateIntoString(String temlatePath, Map<String, Object> model) {
		String text = "";
		Template tpl = null;
		try {
			Map<String, Object> _model = new HashMap<String, Object>();

			/**
			 * 将静态方法暴露给Freemarker
			 */
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModels = wrapper.getStaticModels();
			TemplateHashModel resize = (TemplateHashModel) staticModels.get(ResizeTagHelpers.class.getName());
			_model.put(RESIZE, resize);
			if (staticModels != null) {
				for (Map.Entry<String, String> m : this.staticModels.entrySet()) {
					TemplateHashModel tsh = (TemplateHashModel) staticModels.get(m.getValue());
					_model.put(m.getKey(), tsh);
				}
			}

			_model.putAll(publicDatas);
			_model.put(URL_HELPER, urlHelper);

			if (!Validators.isEmptyOrNull(model)) {
				_model.putAll(model);
			}
			tpl = freeMarker.getConfiguration().getTemplate(temlatePath, LocaleContextHolder.getLocale());
			text = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, _model);
		} catch (Exception e) {
			throw new SystemException(e);
		}
		return text;
	}

	public void setStaticModels(Map<String, String> staticModels) {
		this.staticModels = staticModels;
	}

	public void setPublicDatas(Map<String, Object> publicDatas) {
		this.publicDatas = publicDatas;
	}

	@Override
	public void refresh() throws Exception {
		CacheStorage cs = freeMarker.getConfiguration().getCacheStorage();
		if (cs != null) {
			synchronized (cs) {
				cs.clear();
			}
		}
	}

}
