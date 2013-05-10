$(document).ready(function() {	
	$('#mainSection a.editAction').click(function(){
    	if($(this).closest( "div" ).find( "form" ).length){
    		$(this).closest( "div" ).find( "p" ).hide(); 
        	$(this).closest( "div" ).find( "form" ).show(); 
    	}
    	return false;
    });//end of $('#mainSection div.message').click()
    
	$("#mainSection div.message form input.cancelEdit").click(function(){
        	$(this).parent( "form" ).hide(); 
        	$(this).parent( "form" ).siblings("p").show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
});//end of $(document).ready(fn)