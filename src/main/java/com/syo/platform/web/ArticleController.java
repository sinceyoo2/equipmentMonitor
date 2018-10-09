package com.syo.platform.web;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.syo.platform.entity.Article;
import com.syo.platform.entity.ArticleAttachment;
import com.syo.platform.entity.User;
import com.syo.platform.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService articleService;
	
	@Value("${attachment.article.path}")
	private String attachmentRootPath;
	
	@Value("${attachment.article.default-disk}")
	private String defaultDisk;
	
	@PostMapping("/delete/{id}")
	@ResponseBody
	public String delete(@PathVariable("id")String id, HttpServletRequest request) {
		articleService.deleteArticle(id, request.getSession().getServletContext().getRealPath("/"));
//		return "SUCCESS";
		return "操作成功";
	}
	
	@PostMapping("/delete_attach/{id}")
	@ResponseBody
	public String deleteAttachment(@PathVariable("id")String id) {
		articleService.deleteAttachment(id);
//		return "SUCCESS";
		return "操作成功";
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id")String id, Model model) {
		model.addAttribute("article", articleService.findArticleById(id));
		return "article_detail";
	}
	
	@RequestMapping({"","/","/articles"})
	public String articles(@RequestParam(defaultValue="1")int pageNo, @RequestParam(defaultValue="1")int pageNo2, @RequestParam(defaultValue="5")int pageSize, 
							Date start, Date end, String vague, String author, String currTab, Model model, HttpSession session) {
		String account = ((User)session.getAttribute("loginUser")).getAccount();
		model.addAttribute("vague", vague);
		model.addAttribute("author", author);
		if(currTab==null || currTab.trim().equals("")) {
			currTab = "all";
		}
		model.addAttribute("currTab", currTab);
		
		if(StringUtils.isNotBlank(author)) {
			model.addAttribute("allArticles", articleService.findArticleByAccount(author, start, end, pageNo, pageSize, vague));
		} else {
			model.addAttribute("allArticles", articleService.findArticle(start, end, pageNo, pageSize, vague));
		}
		model.addAttribute("myArticles", articleService.findArticleByAccount(account, start, end, pageNo, pageSize, vague));
//		model.addAttribute("myArticles", articleService.findArticleByAccount("admin", start, end, pageNo2, pageSize, vague));
		return "articles";
	}
	
	@RequestMapping("/writing/{id}")
	public String writeArticle(@PathVariable("id")String id, Model model) {
		Article article = articleService.findArticleById(id);
		model.addAttribute("article", article);
		return "article_writing";
	}
	
	@RequestMapping("/writing")
	public String writeArticle(Model model) {
		model.addAttribute("article", new Article());
		return "article_writing";
	}
	
	@RequestMapping("/save")
	public String save(Article article, @RequestParam("atts")MultipartFile[] attachments, HttpSession session) {
		String account = ((User)session.getAttribute("loginUser")).getAccount();
		User u = new User();
		u.setAccount(account);
		article.setAuthor(u);
		
		List<MultipartFile> attFiles = new ArrayList<>();
		List<File> dirs = new ArrayList<>();
		List<File> targetFiles = new ArrayList<>();
		
		List<ArticleAttachment> atts = new ArrayList<>();
		
		if(attachments.length>0) {
			for(MultipartFile attachment : attachments) {
				if(attachment.getSize()>0) {
					
					String trueFileName = attachment.getOriginalFilename();	
					String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
					String fileName = UUID.randomUUID()+suffix;
					
					ArticleAttachment att = new ArticleAttachment();
					att.setFileName(attachment.getOriginalFilename());
					att.setFileType(attachment.getContentType());
					att.setFileSize(attachment.getSize());
					att.setDisk(defaultDisk+":");
					String path = attachmentRootPath+new SimpleDateFormat("yyyyMMdd").format(att.getCreateTime())+"/";
					att.setFilePath(path+fileName);
					
					atts.add(att);
					File dir = new File(att.getDisk()+path);
					File targetFile = new File(att.getDisk()+att.getFilePath());
					dirs.add(dir);
					targetFiles.add(targetFile);
					attFiles.add(attachment);
				}
			}
		}
		articleService.saveArticle(article, atts);
		if(attFiles!=null &&  attFiles.size()>0) {
			for(int i=0; i<attFiles.size(); i++) {
				File dir = dirs.get(i);
				File targetFile = targetFiles.get(i);
				MultipartFile attachment = attFiles.get(i);
				
				if(!dir.exists()) {
					dir.mkdirs();
				}
				//保存
				try {
					attachment.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
		
		return "redirect:writing";
	}
	
	/*@RequestMapping("/save")
	public String save(Article article, MultipartFile attachment, HttpSession session) {
		String account = ((User)session.getAttribute("loginUser")).getAccount();
		User u = new User();
		u.setAccount(account);
		article.setAuthor(u);

		File dir = null;
		File targetFile = null;
		List<ArticleAttachment> atts = new ArrayList<>();
		if(attachment.getSize()>0) {
			String trueFileName = attachment.getOriginalFilename();		
			String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
			String fileName = UUID.randomUUID()+suffix;
			
			ArticleAttachment att = new ArticleAttachment();
			att.setFileName(attachment.getOriginalFilename());
			att.setFileType(attachment.getContentType());
			att.setFileSize(attachment.getSize());
//			att.setDisk("G:");
			att.setDisk(defaultDisk+":");
			String path = attachmentRootPath+new SimpleDateFormat("yyyyMMdd").format(att.getCreateTime())+"/";
			att.setFilePath(path+fileName);
//			att.setArticle(article);

			atts.add(att);
//			article.setAttachments(atts);
			
			dir = new File(att.getDisk()+path);
			targetFile = new File(att.getDisk()+att.getFilePath());
		}
		
		articleService.saveArticle(article, atts);
		
		if(dir!=null && targetFile!=null) {
			if(!dir.exists()) {
				dir.mkdirs();
			}
			//保存
			try {
				attachment.transferTo(targetFile);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	
	
		return "redirect:writing";
	}*/
	
	@RequestMapping("/editormdPic")
	@ResponseBody
	public Map<String, Object> editormdPic(@RequestParam(value="editormd-image-file",required=true)MultipartFile file, HttpServletRequest request) {
		
		String trueFileName = file.getOriginalFilename();
		
		String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
		String fileName = UUID.randomUUID()+suffix;
		
		User loginUser = (User) request.getSession().getAttribute("loginUser");
		String path = request.getSession().getServletContext().getRealPath("/material/"+loginUser.getAccount()+"/article/image/");
		
//		String path = request.getSession().getServletContext().getRealPath("/material/admin/article/image/");
		
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File targetFile = new File(path, fileName);
		
		//保存
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		Map<String, Object> res = new HashMap<String, Object>();
//		res.put("url", "material/"+loginUser.getAccount()+"/article/image/"+fileName);
		res.put("url", "material/admin/article/image/"+fileName);
		res.put("success", 1);
		res.put("message", "upload success!");
		
		return res;
	}
	
	@RequestMapping("/download/{attchId}")
	public String downAttchment(@PathVariable("attchId")String attchId, HttpServletRequest req) {
		ArticleAttachment atth = articleService.findAttchById(attchId);
		req.setAttribute("fileName", atth.getFileName());
		req.setAttribute("filePath", atth.getDisk()+"/"+atth.getFilePath());
		return "forward:/download";
	}
	
}
