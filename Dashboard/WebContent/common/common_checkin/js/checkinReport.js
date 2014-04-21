
function printError(tag,message,id)
{	
	deleteError(id);
	var errTag = document.createElement('p');
	errTag.className = "myerror";
	errTag.setAttribute("id", id);
	errTag.innerHTML = message;
	var node = document.getElementById(tag);
	node.parentNode.insertBefore(errTag,node);
}

function deleteError(id)
{
	if(document.getElementById(id))
	{
		var element = document.getElementById(id);		
		element.parentNode.removeChild(element);
	}	
	return;
}


function cleanString(str)
{
	str = str.replace(/\r/g, "");
	while (/\s\n/.test(str)) {
	str = str.replace(/\s\n/g, "\n");
	} 
	while (/\n\n/.test(str)) {
	str = str.replace(/\n\n/g, "\n");
	} 
	return str.replace(/\n/g,'%##').replace(/\s+/g,'').replace(/\s*%##$/g,'').split("%##");
}


function testlisting() {
	var ele = document.getElementById("testcaselisting_itemid"); 
	var arr = cleanString(ele.value);
	result = [];
	for (var i in arr){
		if(arr[i].indexOf(".java")==-1 && arr[i].indexOf(":")==-1)
			result.push(arr[i]);
	}
	
	if(result.length!=0)
	{
		document.getElementById("test_countid").setAttribute("value",result.length);
		deleteError("testcaselisting_itemid_error");
	}
	else
		printError("testcase_listingdiv","Invalid Testcase Lising","testcase_listingdiv_error");

}

function txnName() {
	str = document.getElementById("file_affectedid").value;	
	var result = str.substring(str.search(/Transaction [^A-Z]* successfully ended./g),str.length).replace(/Transaction /g,'').replace(/ successfully ended./g,'');
	document.getElementById("txn_nameid").setAttribute("value",result);
	
	
	//label merged
	str = document.getElementById("file_affectedid").value;
	
	if(str.search(/View restored to label [^%]*/g)!=-1)
	{
		str = str.substring(str.search(/View restored to label [^%]*/g),str.length);
		if(str.search(/\[/g)!=-1 || str.search(/\]/g)!=-1)
		{
			str = str.substring(str.search(/\[/g)+1,str.search(/\]/g));
			deleteError("testcaselisting_itemid_error");
			document.getElementById("label_mergedid").setAttribute("value", str);
		}
		else
			printError("file_affecteddiv","Invalid End Merge Log","file_affecteddiv_error");
	}
	else
		printError("file_affecteddiv","Invalid End Merge Log","file_affecteddiv_error");
}



function validateOrareview()
{
	var orareview = document.getElementById("ora_reviewid");
	var text = orareview.value;

	if(text.indexOf("orareview")==-1 || text.indexOf("http")==-1 ||
			 text.indexOf(".com")==-1 || text.indexOf("oracle")==-1)
		printError("orareview_div","Invalid orareview Link","orareview_div_error");
	else
		deleteError("orareview_div_error");
}

function init(){	
	
	document.getElementById("ora_reviewid").addEventListener("change", validateOrareview);
	document.getElementById("file_affectedid").addEventListener("change", txnName);
	document.getElementById("testcaselisting_itemid").addEventListener("change", testlisting);
	
}
