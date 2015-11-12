package me.qyh.helper.freemaker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import me.qyh.exception.SystemException;
import me.qyh.utils.Validators;
import me.qyh.web.tag.url.UrlHelper;

/**
 * 用来解析信息,邮件模板
 * 
 * @author mhlx
 *
 */
public class WebFreemarkers {

	private static final String URL_HELPER = "urlHelper";

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
	public String processTemplateIntoString(String temlatePath,
			Map<String, Object> model) {
		String text = "";
		Template tpl = null;
		try {
			Map<String, Object> _model = new HashMap<String, Object>();

			/**
			 * 将静态方法暴露给Freemarker
			 */
			if(!Validators.isEmptyOrNull(staticModels)){
				BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
				TemplateHashModel staticModels = wrapper.getStaticModels();
				for (Map.Entry<String, String> m : this.staticModels.entrySet()) {
					TemplateHashModel tsh = (TemplateHashModel) staticModels
							.get(m.getValue());
					_model.put(m.getKey(), tsh);
				}
			}

			_model.putAll(publicDatas);
			_model.put(URL_HELPER, urlHelper);

			if (!Validators.isEmptyOrNull(model)) {
				_model.putAll(model);
			}

			tpl = freeMarker.getConfiguration().getTemplate(temlatePath,
					LocaleContextHolder.getLocale());
			text = FreeMarkerTemplateUtils.processTemplateIntoString(tpl,
					_model);
		} catch (IOException | TemplateException e) {
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

}
