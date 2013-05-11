$(document).ready(function() {	
	$('#mainSection a.editAction').click(function(){
		var objet = $(this).closest( "div" ).find( "form" );
    	if(objet.length){
    		objet.find( "p" ).hide(); 
    		objet.find( "div" ).show(); 
    	}
    	return false;
    });//end of $('#mainSection div.message').click()
    
	$("#mainSection div.message form input.cancelEdit").click(function(){
        	$(this).parent( "div" ).hide(); 
        	$(this).parent( "div" ).siblings("p").show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
});//end of $(document).ready(fn)