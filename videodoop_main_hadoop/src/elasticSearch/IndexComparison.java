package elasticSearch;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.facet.Facet;
import org.elasticsearch.search.facet.FacetBuilders;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.facet.terms.TermsFacet;

public class IndexComparison {

	public int compare_reject(String filename, int frameno, String vector,Client client)
	{
		//-1 = reject
		// 1 = accept
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		StringTokenizer token = new StringTokenizer(vector,"\t");
		
		String color = token.nextToken();
		
		map.put("corner", token.nextToken());
		map.put("apidq", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
		map.put("hsv", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
		map.put("edge", token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken()+"a"+token.nextToken());
		map.put("frameno", frameno);
		//System.err.println(map.get("corner"));
		//System.err.println(map.get("apidq"));
		//System.err.println(map.get("hsv"));
		//System.err.println(map.get("edge"));
		
		
		SearchRequestBuilder builder = client.prepareSearch("movie").setTypes(color);
		QueryBuilder query = QueryBuilders.customScoreQuery(QueryBuilders.queryString("filename:"+filename.replace(".", ""))).script("compareScript").lang("native").params(map);
		builder.setQuery(query);
		builder.addFacet(FacetBuilders.termsFacet("filename").field("filename"));
		builder.setExplain(true);
		builder.setSize(10);
		builder.execute();
		
		SearchResponse rsp = builder.execute().actionGet();
		
		if(rsp!=null)
		{
			Facets facets = rsp.facets();
			 if (facets != null)
	                for (Facet facet : facets.facets())
	                {
	                	 TermsFacet ff = (TermsFacet) facet;	
	                	 System.err.print(ff.getTotalCount());
	                	 for (TermsFacet.Entry e : ff.entries()) {
	                		 System.err.print(e.getTerm()+"----"+e.getCount()+"\n");
	                	 }
	                }
		}
		
		SearchHit[] docs = rsp.getHits().getHits();
	//	System.err.println("---");
		for(SearchHit d1: docs)
		{
			//System.err.println(color+"  "+filename+"   "+d1.getSource().get("filename").toString()+"  "+ d1.getScore()+"  "+d1.getSource().get("color").toString());
			if(d1.getScore()>=85.0f)				
				return -1;
		}
		return 1;
	}
	
}
