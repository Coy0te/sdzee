$(document).ready(function() {	
	$('#mainSection div.message').click(function(){
    	if($(this).children( "form" ).length){
        	$(this).children( "p" ).hide(); 
        	$(this).children( "form" ).show(); 
    	}
    	return false;
    });//end of $('#mainSection div.message').click()
    
});//end of $(document).ready(fn)