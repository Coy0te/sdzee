$(document).ready(function() {	
	$('#mainSection div.message').click(function(){
    	var currentId = $(this).attr('id').match(/^msg([0-9]+)$/);
    	var messageId = currentId[1];
    	
    	//TODO :
    	// - click IN, masquage du message et affichage de la textArea (préparée dans la page JSF mais masquée via CSS)
    	// - clock OUT, réaffichage du contenu d'origine.
    	$(this).children( "p" ).hide(); 
    	$(this).children( "form" ).show(); 

    	return false;
    });
    
});//end of $(document).ready(fn)