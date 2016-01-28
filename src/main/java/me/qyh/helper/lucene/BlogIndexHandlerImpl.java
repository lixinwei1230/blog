package me.qyh.helper.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.LimitTokenCountAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import me.qyh.bean.Scopes;
import me.qyh.dao.BlogDao;
import me.qyh.entity.Scope;
import me.qyh.entity.Space;
import me.qyh.entity.blog.Blog;
import me.qyh.entity.blog.BlogCategory;
import me.qyh.entity.blog.BlogFrom;
import me.qyh.entity.blog.BlogStatus;
import me.qyh.entity.tag.Tag;
import me.qyh.exception.SystemException;
import me.qyh.helper.excutor.ExcutorManager;
import me.qyh.pageparam.BlogPageParam;
import me.qyh.pageparam.Order;
import me.qyh.pageparam.OrderType;
import me.qyh.pageparam.Page;
import me.qyh.utils.Validators;

@Component
public class BlogIndexHandlerImpl
		implements BlogIndexHandler, InitializingBean, ApplicationListener<ApplicationContextEvent> {

	private static final Logger logger = LoggerFactory.getLogger(WriteToIndexOperation.class);
	private final ReadWriteLock rwl = new ReentrantReadWriteLock();
	@Value("${config.blog.index.dir}")
	private String indexDir;
	@Autowired
	private ExcutorManager excutorManager;
	private IndexReader reader;
	private int maxResults = 1000;
	private int maxTokenCount = 1000;
	/**
	 * 数据量不大且当前没有为博客建立索引时候使用
	 */
	private boolean cleanAndBuildAllBlogsWhenContextStart = true;

	protected static final String ID = "id";
	protected static final String SPACE = "space";
	protected static final String TITLE = "title";
	protected static final String CATEGORY_ID = "category";
	protected static final String CATEGORY_NAME = "categoryName";
	protected static final String SCOPE = "scope";
	protected static final String RECOMMEND = "recommend";
	protected static final String STATUS = "status";
	protected static final String FROM = "from";
	protected static final String LEVEL = "level";
	protected static final String HITS = "hits";
	protected static final String WRITE_DATE = "writeDate";
	protected static final String TAGS = "tags";
	protected static final String DEL = "del";

	private final SortPro[] pros = new SortPro[] { new SortPro(HITS, Type.INT) };

	protected Analyzer getAnalyzer() {
		return new StandardAnalyzer();
	}

	protected SortPro[] getSortPros() {
		return pros;
	}

	private IndexReader getIndexReader() {
		try {
			if (reader == null) {
				reader = DirectoryReader.open(getIndexDirectory());
			} else {
				IndexReader newReader = DirectoryReader.openIfChanged((DirectoryReader) reader);
				if (newReader != null) {
					reader.close();
					reader = newReader;
				}
			}
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
		return reader;
	}

	private Directory getIndexDirectory() {
		try {
			return FSDirectory.open(new File(indexDir).toPath());
		} catch (IOException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}

	protected Document buildDocument(Blog blog) {
		Document doc = new Document();
		doc.add(new StringField(ID, blog.getId().toString(), Field.Store.YES));
		doc.add(new StringField(SPACE, blog.getSpace().getId(), Field.Store.NO));
		doc.add(new StringField(TITLE, blog.getTitle(), Field.Store.NO));
		doc.add(new SortedDocValuesField(CATEGORY_NAME, new BytesRef(blog.getCategory().getName())));
		doc.add(new StoredField(CATEGORY_NAME, blog.getCategory().getName()));
		doc.add(new StringField(CATEGORY_ID, blog.getCategory().getId().toString(), Field.Store.NO));
		doc.add(new StringField(SCOPE, blog.getScope().name().toLowerCase(), Field.Store.NO));
		Integer level = blog.getLevel();
		doc.add(new StringField(RECOMMEND, blog.isRecommend() + "", Field.Store.NO));
		doc.add(new StringField(STATUS, blog.getStatus().name().toLowerCase(), Field.Store.NO));
		doc.add(new StringField(FROM, blog.getFrom().name().toLowerCase(), Field.Store.NO));
		doc.add(new NumericDocValuesField(LEVEL, (level == null ? -1 : level)));
		doc.add(new StoredField(LEVEL, blog.getLevel()));
		doc.add(new NumericDocValuesField(HITS, blog.getHits()));
		doc.add(new StoredField(HITS, blog.getHits()));
		long writeTime = blog.getWriteDate().getTime();
		doc.add(new NumericDocValuesField(WRITE_DATE, writeTime));
		doc.add(new LongField(WRITE_DATE, writeTime, Field.Store.NO));
		doc.add(new StringField(DEL, blog.getDel().toString(), Field.Store.NO));
		Set<Tag> tags = blog.getTags();
		if (!Validators.isEmptyOrNull(tags)) {
			for (Tag tag : tags) {
				doc.add(new StringField(TAGS, tag.getName(), Field.Store.YES));
			}
		}
		return doc;
	}

	public Page<Blog> search(BlogPageParam param) {
		IndexSearcher searcher = new IndexSearcher(getIndexReader());
		Sort sort = parseSort(param);
		Query query = parseBlogPageParam(param);
		try {
			TopDocs tds = sort == null ? searcher.search(query, maxResults) : searcher.search(query, maxResults, sort);
			int total = tds.totalHits;
			int offset = param.getOffset();
			List<Blog> datas = new ArrayList<Blog>();
			if (offset < total) {
				ScoreDoc[] docs = tds.scoreDocs;
				int last = offset + param.getPageSize();
				for (int i = offset; i < Math.min(Math.min(last, total), maxResults); i++) {
					datas.add(parseToBlog(searcher.doc(docs[i].doc)));
				}
			}
			return new Page<Blog>(param, Math.min(maxResults, total), datas);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	private SortPro getPro(String property) {
		SortPro[] pros = getSortPros();
		if (!Validators.isEmptyOrNull(pros)) {
			for (SortPro pro : pros) {
				if (pro.getPro().equalsIgnoreCase(property)) {
					return pro;
				}
			}
		}
		return null;
	}

	protected Sort parseSort(BlogPageParam param) {
		List<SortField> fields = new ArrayList<SortField>();
		if (param.getSpace() != null && !param.isIgnoreLevel()) {
			fields.add(new SortField(LEVEL, Type.INT, true));
		}
		Set<Order> orders = param.getOrders();
		if (!Validators.isEmptyOrNull(orders)) {
			for (Order order : orders) {
				String property = order.getProperty();
				OrderType ot = order.getType();
				SortPro pro = null;
				if (!Validators.isEmptyOrNull(property, true) && ot != null && (pro = getPro(property)) != null) {
					fields.add(new SortField(pro.getPro(), pro.getType(), ot.equals(OrderType.DESC)));
				}
			}
		}
		fields.add(new SortField(WRITE_DATE, SortField.Type.LONG, true));
		return new Sort(fields.toArray(new SortField[fields.size()]));
	}

	protected Query parseBlogPageParam(BlogPageParam param) {
		Builder builder = new Builder();
		Space space = param.getSpace();
		if (space != null) {
			Query query = new TermQuery(new Term(SPACE, space.getId()));
			builder.add(query, Occur.MUST);
		}
		Date begin = param.getBegin();
		Date end = param.getEnd();
		boolean dateRangeQuery = (begin != null && end != null);
		if(dateRangeQuery){
			Query query = NumericRangeQuery.newLongRange(WRITE_DATE, begin.getTime() , end.getTime(), true, true);
			builder.add(query, Occur.MUST);
		}
		Boolean del = param.getDel();
		if(del != null){
			Query query = new TermQuery(new Term(DEL, del.toString()));
			builder.add(query, Occur.MUST);
		}
		BlogCategory cate = param.getCategory();
		if (cate != null && cate.hasId()) {
			Query query = new TermQuery(new Term(CATEGORY_ID, cate.getId().toString()));
			builder.add(query, Occur.MUST);
		}
		BlogFrom from = param.getFrom();
		if (from != null) {
			Query query = new TermQuery(new Term(FROM, from.name().toLowerCase()));
			builder.add(query, Occur.MUST);
		}
		BlogStatus status = param.getStatus();
		if (status != null) {
			Query query = new TermQuery(new Term(STATUS, status.name().toLowerCase()));
			builder.add(query, Occur.MUST);
		}
		Boolean recommend = param.getRecommend();
		if (recommend != null) {
			Query query = new TermQuery(new Term(RECOMMEND, recommend.toString()));
			builder.add(query, Occur.MUST);
		}
		String title = param.getTitle();
		if (!Validators.isEmptyOrNull(title, true)) {
			QueryParser parser = new QueryParser(TITLE, getAnalyzer());
			try {
				builder.add(parser.parse(title), Occur.MUST);
			} catch (ParseException e) {
				// ignore
			}
		}
		Scopes scopes = param.getScopes();
		if (scopes != null && !scopes.isEmpty()) {
			List<Scope> _scopes = scopes.toList();
			Builder scopeBuilder = new Builder();
			for (Scope scope : _scopes) {
				scopeBuilder.add(new TermQuery(new Term(SCOPE, scope.name().toLowerCase())), Occur.SHOULD);
			}
			builder.add(scopeBuilder.build(), Occur.MUST);
		}
		Set<String> tags = param.getTags();
		if (!Validators.isEmptyOrNull(tags)) {
			Builder tagBuilder = new Builder();
			for (String tag : tags) {
				tagBuilder.add(new FuzzyQuery(new Term(TAGS, tag + "~")), Occur.SHOULD);
			}
			builder.add(tagBuilder.build(), Occur.MUST);
		}
		return builder.build();
	}

	private void excute(WriteToIndexOperation op, boolean wait) {
		try {
			if (wait) {
				excutorManager.excuteInForeground(op);
			} else {
				excutorManager.excute(op);
			}
		} catch (InterruptedException e) {
			throw new SystemException(e);
		}
	}

	private void excute(WriteToIndexOperation op) {
		excute(op, false);
	}

	public void rebuildBlogIndex(Blog... blogs) {
		excute(new ReBlogIndexOperation(Arrays.asList(blogs)));
	}

	public void removeBlogIndex(Blog... blogs) {
		excute(new RemoveBlogIndexOperation(Arrays.asList(blogs)), true);
	}

	private abstract class WriteToIndexOperation implements Runnable {

		protected List<Blog> blogs;

		public WriteToIndexOperation(List<Blog> blogs) {
			this.blogs = blogs;
		}

		protected IndexWriter getWriter() {
			try {
				LimitTokenCountAnalyzer analyzer = new LimitTokenCountAnalyzer(getAnalyzer(), maxTokenCount);
				IndexWriterConfig config = new IndexWriterConfig(analyzer);
				return new IndexWriter(getIndexDirectory(), config);
			} catch (IOException e) {
				throw new SystemException(e.getMessage(), e);
			}
		}

		protected void closeWriter(IndexWriter writer) {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		public void run() {
			if (!Validators.isEmptyOrNull(blogs)) {
				try {
					rwl.writeLock().lock();
					doRun();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					rwl.writeLock().unlock();
				}
			}
		}

		abstract void doRun() throws Exception;
	}

	private class ReBlogIndexOperation extends WriteToIndexOperation {

		public ReBlogIndexOperation(List<Blog> blogs) {
			super(blogs);
		}

		@Override
		void doRun() throws Exception {
			IndexWriter writer = getWriter();
			try {
				if (writer != null) {
					for (Blog blog : blogs) {
						Term term = new Term(ID, blog.getId().toString());
						writer.deleteDocuments(term);
						writer.addDocument(buildDocument(blog));
					}
				}
			} finally {
				closeWriter(writer);
			}
		}
	}

	private class RemoveBlogIndexOperation extends WriteToIndexOperation {

		public RemoveBlogIndexOperation(List<Blog> blogs) {
			super(blogs);
		}

		@Override
		void doRun() throws Exception {
			IndexWriter writer = getWriter();
			try {
				if (writer != null) {
					for (Blog blog : blogs) {
						Term term = new Term(ID, blog.getId().toString());
						writer.deleteDocuments(term);
					}
					writer.commit();
				}
			} finally {
				closeWriter(writer);
			}
		}
	}

	private final class DeleteAllAndRebuildBlogsOperation extends WriteToIndexOperation {

		public DeleteAllAndRebuildBlogsOperation(List<Blog> blogs) {
			super(blogs);
		}

		@Override
		void doRun() throws Exception {
			IndexWriter writer = getWriter();
			try {
				if (writer != null) {
					writer.deleteAll();
					for (Blog blog : blogs) {
						writer.addDocument(buildDocument(blog));
					}
				}
			} finally {
				closeWriter(writer);
			}
		}

	}

	protected class SortPro {

		private String pro;
		private Type type;

		public String getPro() {
			return pro;
		}

		public void setPro(String pro) {
			this.pro = pro;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public SortPro(String pro, Type type) {
			super();
			this.pro = pro;
			this.type = type;
		}
	}

	protected boolean indexExists(Directory dir) {
		try {
			return DirectoryReader.indexExists(dir);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	protected void createIndex(Directory dir) {
		if (!indexExists(dir)) {
			IndexWriter writer = null;
			try {
				IndexWriterConfig config = new IndexWriterConfig(
						new LimitTokenCountAnalyzer(getAnalyzer(), maxTokenCount));

				writer = new IndexWriter(dir, config);
			} catch (IOException e) {
				throw new SystemException(e.getMessage(), e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!cleanAndBuildAllBlogsWhenContextStart) {
			createIndex(getIndexDirectory());
		}
	}

	private boolean isRootApplicationContext(ApplicationContext ctx) {
		return (ctx.getParent() == null);
	}

	@Override
	public void onApplicationEvent(ApplicationContextEvent event) {
		ApplicationContext ctx = event.getApplicationContext();
		if (isRootApplicationContext(ctx)) {
			if (event instanceof ContextClosedEvent) {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
			if (cleanAndBuildAllBlogsWhenContextStart && (event instanceof ContextRefreshedEvent)) {
				cleanAndBuildAllBlogsWhenContextStart = false;
				if (isRootApplicationContext(ctx)) {
					BlogDao blogDao = ctx.getBean(BlogDao.class);
					BlogPageParam param = new BlogPageParam();
					param.setCurrentPage(1);
					param.setPageSize(Integer.MAX_VALUE);
					param.setStatus(null);
					List<Blog> blogs = blogDao.selectPage(param);
					try {
						excutorManager.excute(new DeleteAllAndRebuildBlogsOperation(blogs));
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	protected Blog parseToBlog(Document doc) {
		Blog blog = new Blog(Integer.parseInt(doc.get(ID)));
		IndexableField[] tagFields = doc.getFields(TAGS);
		if (!Validators.isEmptyOrNull(tagFields)) {
			for (IndexableField field : doc.getFields(TAGS)) {
				Tag tag = new Tag();
				tag.setName(field.stringValue());
				blog.addTags(tag);
			}
		}
		return blog;
	}

	/**
	 * 数据量不大且当前没有为博客建立索引时候使用
	 */
	public void setCleanAndBuildAllBlogsWhenContextStart(boolean cleanAndBuildAllBlogsWhenContextStart) {
		this.cleanAndBuildAllBlogsWhenContextStart = cleanAndBuildAllBlogsWhenContextStart;
	}
}
