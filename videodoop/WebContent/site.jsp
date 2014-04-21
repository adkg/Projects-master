<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Videodoop</title>
	<link href="css/style.css" rel="stylesheet" type="text/css" media="screen" />
	<link href="css/basic.css" type="text/css" rel="stylesheet" />
	<link href="css/enhanced.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="scripts/enhance.js"></script>		
		<script type="text/javascript" src="scripts/jquery.min.js"></script>
		<script type="text/javascript" src="scripts/jQuery.fileinput.js"></script>
		<script type="text/javascript" src="scripts/example.js"></script>
</head>

<body>
	<header id="site_head">
		<div class="header_cont">
        	<h1><a href="#">Videodoop</a></h1>          
                <nav class="head_nav">
                <ul>
                    <li><a href="#">Doop it!</a></li>
                    <li><a href="#">About Us</a></li>
                </ul>
                </nav>
        
		</div>
	</header>
    
    <div id="main_content">
    <center>
    	<div class="logo">
        	<figure>
						<img src="images/logo.png" alt="test" />
			</figure>
        </div>
        
        
        <div class="upload">        
        <center>	
        	<FORM  ENCTYPE="multipart/form-data" ACTION= "file_upload" METHOD=POST>
                <fieldset >
				<label for="file">Upload Video</label>
				<input type="file" name="file" id="file" />
				<input type="submit" name="upload" id="upload" value="Upload Video" />
			</fieldset>
     	</FORM> 
     	</center>       
        </div>
        
        
        
        
    </center>
    <footer class="bottom">
		<!--footer info here-->
	</footer>
    
    </div>

</body>
</html>
