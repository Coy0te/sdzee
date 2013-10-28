$(document).ready(function() {
  initAll();
});//end of $(document).ready(fn)

function initAll(){	
	styleCode();
	
	 $("#dialog").dialog({
		    modal: true,
		    resizable: false,
		    draggable: false,
		    width: 500,
		    height: 200,
		    autoOpen: false
	 });//end of $('#dialog').dialog()
	
	$('#mainSection a.editAction').click( function(e){
		e.preventDefault();
		var objet = $(this).closest( "div" ).find( "form" );
    	if(objet.length){
    		objet.find( "div.markdownBody" ).hide(); 
    		objet.find( "div.hidden" ).show();
    	}
    	return false;
    });//end of $('#mainSection a.editAction').click()
	
	$('#mainSection a.alertAction').on( 'click', function(e){
		e.preventDefault();
		//TODO: pourquoi closest trouve pas le truc ?
		var idObjet = $(this).closest( "div.message" ).attr( "id" ).match(/\((-?\d+)\)/)[1];

	    $("#dialog").dialog('option', 'buttons', {
	      "Confirmer": function () {
	        //TODO : ajax call pour sujet d'id = idObjet.
	    	alert("Alerte sur l'objet d'id : " + idObjet);
	      },
	      "Annuler": function () {
	        $(this).dialog("close");
	      }
	    });

	    $("#dialog").dialog("open");
    });//end of $('#mainSection a.alertAction').click()
    
	$('#mainSection div.message form input.cancelEdit').on( 'click', function(e){
		e.preventDefault();
		$(this).parent( "div.hidden" ).hide(); 
        $(this).parent( "div" ).siblings( "div.markdownBody" ).show(); 
    	return false;
    });//end of $('#mainSection div.message').click()
	
	$('#mainSection a.citeAction').click( function(){
		var texteBrut = $.trim( $(this).closest( "div.comment" ).find( "div.bruteMarkdownBody" ).html() );
		var auteur = $.trim( $(this).closest( "div.row" ).find( "div.author a" ).html() );
		texteBrut = '>'+texteBrut.replace(/\n/g,"\n>");
		$('#texte').val( $('#texte').val() + '\n**' + auteur + ' a écrit :**\n' );
		$('#texte').val( $('#texte').val() + texteBrut );
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