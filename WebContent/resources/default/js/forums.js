$(document).ready(function() {	
	$('#mainSection div.message').dblclick(function(){
    	var currentId = $(this).attr('id').match(/^msg([0-9]+)$/);
    	var messageId = currentId[1];
    	
    	//TODO :
    	// - click IN, masquage du message et affichage de la textArea (préparée dans la page JSF mais masquée via CSS)
    	// - clock OUT, réaffichage du contenu d'origine.
    	$(this).child( "form" ).show(); 
    	html("<textarea>Bip bip !</textarea>");


    	return false;
    });
    
});//end of $(document).ready(fn)