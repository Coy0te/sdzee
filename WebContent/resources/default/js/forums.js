$(document).ready(function() {
  initAll();
});//end of $(document).ready(fn)

function initAll(){	
	$('#mainSection a.editAction').click( function(e){
		e.preventDefault();
		var objet = $(this).closest( "div" ).find( "form" );
    	if(objet.length){
    		objet.find( "p" ).hide(); 
    		objet.find( "div" ).show(); 
    	}
    	return false;
    });//end of $('#mainSection div.message').click()
    
	$('#mainSection div.message form input.cancelEdit').on( 'click', function(e){ // le on() devrait pas gérer le live/ajax re-render ?
		e.preventDefault();
		$(this).parent( "div" ).hide(); 
        $(this).parent( "div" ).siblings("p").show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
}//end of initAll()

/*
 * Callback pour <f:ajax> : permet de recharger les handlers jQuery après un render Ajax via JSF.
 * Sans cette fonction, les selecteurs jQuey appliqués aux éléments du DOM qui sont rechargés par
 * l'appel à <f:ajax> sont perdus et ne fonctionnent plus ensuite.
 */
function initAllCallback(e) {
	if(e.status == "success") {  
		initAll();
	}
}