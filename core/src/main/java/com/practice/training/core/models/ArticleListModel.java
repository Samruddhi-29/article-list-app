
package com.practice.training.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.spi.commons.query.sql.QueryBuilder;
import org.apache.log4j.Logger;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.service.log.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.Search.Hit;
import com.practice.training.core.beans.ArticleListDataBean;

@Model(adaptables = Resource.class , defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListModel {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ArticleListModel.class);

    @Inject
    private String articlesRootPath;

    @Inject
    private String title;

    @Self
    Resource resource;

    List<ArticleListDataBean> articleListDataBeans ;

    @PostConstruct
    private void init() {

        ResourceResolver resourceResolver = resource.getResourceResolver();

        Map<String,String> predicate = new HashMap<>();

        predicate.put("path", articlesRootPath);

        predicate.put("type", "cq:Page");

        Session session = resourceResolver.adaptTo(Session.class);

        Query query = null;

        com.day.cq.search.QueryBuilder queryBuilder = resourceResolver.adaptTo(com.day.cq.search.QueryBuilder.class);

        try{
            query = queryBuilder.createQuery(PredicateGroup.create(predicate), session);
        }
        catch(Exception e){

            LOGGER.error("Error in query ", e);

        }

        SearchResult searchResult = query.getResult();
        articleListDataBeans = new ArrayList<>();



        for(com.day.cq.search.result.Hit hit : searchResult.getHits()){

            String path = "";

            try{

                ArticleListDataBean articleListDataBean = new ArticleListDataBean();
                path = hit.getPath();

                Resource articlResource = resourceResolver.getResource(path);
                Page articlPage = articlResource.adaptTo(Page.class);
                String title = articlPage.getTitle();
                String description = articlPage.getDescription();

                articleListDataBean.setPath(path);
                articleListDataBean.setTitle(title);
                articleListDataBean.setDescription(description);

                articleListDataBeans.add(articleListDataBean);

                LOGGER.debug("Path : {} , Navigation title : {} ,  Description : {}",path,title,description);
            }catch(Exception e){

            }
        }


    }


    public String getTitle() {
        return title;
    }

    public String getArticlesRootPath() {
        return articlesRootPath;
    }

    public List<ArticleListDataBean> getArticleListDataBeans() {
        return articleListDataBeans;
    }


    public boolean isEmpty() {
        return StringUtils.isAllEmpty(articlesRootPath);
    }

}