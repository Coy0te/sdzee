$(document).ready(function() {
  initAll();
});//end of $(document).ready(fn)

function initAll(){	
	
	 $("#dialog").dialog({
		    modal: true,
		    resizable: false,
		    draggable: false,
		    width: 500,
		    height: 200,
		    autoOpen: false
	 });//end of $('#dialog').dialog()
	
	$('#listeCategoriesForums a.editAction').click( function(e){
		e.preventDefault();
		var objet = $(this).closest( "tr" ).find( "form" );
    	if(objet.length){
    		objet.find( "span.titreCategorie" ).hide(); 
    		objet.find( "div.hidden" ).show();
    	}
    	return false;
    });//end of $('#mainSection a.editAction').click()
    
	$('#listeCategoriesForums input.cancelEdit').on( 'click', function(e){
		e.preventDefault();
		$(this).parent( "div.hidden" ).hide(); 
        $(this).closest( "form" ).find( "span.titreCategorie" ).show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
	
   $('#listeCategoriesForums a.lienDeplacerForum').click( function(e){
        e.preventDefault();
        $(this).hide();
        $(this).closest( "form" ).find( "div.hidden2" ).show(); 
        return false;
    });//end of $('#mainSection a.editAction').click()
	
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