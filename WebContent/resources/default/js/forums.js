$(document).ready(function() {
  initAll();
});//end of $(document).ready(fn)

function initAll(){	
	styleCode();
	
	$('#mainSection a.editAction').click( function(e){
		e.preventDefault();
		var objet = $(this).closest( "div" ).find( "form" );
    	if(objet.length){
    		objet.find( "div.markdownBody" ).hide(); 
    		objet.find( "div.hidden" ).show();
    	}
    	return false;
    });//end of $('#mainSection div.message').click()
    
	$('#mainSection div.message form input.cancelEdit').on( 'click', function(e){ // le on() devrait pas gérer le live/ajax re-render ?
		e.preventDefault();
		$(this).parent( "div.hidden" ).hide(); 
        $(this).parent( "div" ).siblings( "div.markdownBody" ).show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
	
	$('#mainSection a.citeAction').click( function(e){
		e.preventDefault();
		var objet = $(this).closest( "div.comment" ).find( "..." );
		//TODO
	});//end of $('#mainSection a.citeAction').click()
	
	$('#mainSection input.voteButton').on( 'mouseover', function(){
		$(this).next( "div.tooltip" ).addClass("in");
	}).on( 'mouseout', function(){
		$(this).next( "div.tooltip" ).removeClass("in");
	});//end of $('#mainSection input.votebutton').mouseover()
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


/*
 * Repris depuis StackOverflow. Trouve toutes les balises <pre><code> 
 * dans la page et ajoute le style "prettyprint linenums". Si au moins
 * une balise est trouvé, alors on fait appel à l'API Google Prettify.
 */
function styleCode() 
{
 if (typeof disableStyleCode != "undefined") 
 {
     return;
 }
 var a = false;
 $("pre code").parent().each(function() 
 {
     if (!$(this).hasClass("prettyprint")) 
     {
         $(this).addClass("prettyprint linenums");
         a = true;
     }
 });
 if (a) { prettyPrint(); } 
}