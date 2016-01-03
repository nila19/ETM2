(function(){
    var d = dojo;
	d.declare("dojo._Blocker", null, {
	 
	    duration: 400,
	    opacity: 0.6,
	    backgroundColor: "#fff",
	    background: "#fff url("+APP_PATH+"/images/loader-big.gif) no-repeat center center",
	    zIndex: 999,
	 
	    constructor: function(node, args) {
	        // mixin the passed properties into this instance
	        dojo.mixin(this, args);
	        this.node = d.byId(node);
	 
	        // create a node for our overlay.
	        this.overlay = dojo.doc.createElement('div');
	 
	        // do some chained magic nonsense
	        dojo.query(this.overlay)
	            .place(dojo.body(),"last")
	            .addClass("dojoBlockOverlay")
	            .style({
	                backgroundColor: this.backgroundColor,
	                background: this.background,
	                position: "absolute",
	                zIndex: this.zIndex,
	                display: "none",
	                opacity: this.opacity
	            });
	    },
	 
	    show: function() {
	        // summary: Show this overlay
	        var pos = dojo.coords(this.node, true),
	            ov = this.overlay;
	     
	        dojo.marginBox(ov, pos);
	        dojo.style(ov, { opacity:0, display:"block" });
	        dojo.anim(ov, { opacity: this.opacity }, this.duration);
	    },
	    
	    hide: function(){
	        // summary: Hide this overlay
	        dojo.fadeOut({
	            node: this.overlay,
	            duration: this.duration,
	            // when the fadeout is done, set the overlay to display:none
	            onEnd: dojo.hitch(this, function(){
	                dojo.style(this.overlay, "display", "none");
	            })
	        }).play();
	    }
	 
	});
    // cache of all blockers:
    var blockers = {};
    d.mixin(d, {
    	  block: function(/* String|DomNode */node, /* dojo.block._blockArgs */args) {
    	    var n = d.byId(node);
    	    var id = d.attr(n, "id");
    	    if(!id){
    	        id = _uniqueId();
    	        d.attr(n, "id", id);
    	    }
    	    if(!blockers[id]){
    	        blockers[id] = new d._Blocker(n, args);
    	    }
    	    blockers[id].show();
    	    return blockers[id]; // dojo._Blocker
    	  },
    	    unblock: function(node, args){
    	        // summary: Unblock the passed node
    	        var id = d.attr(node, "id");
    	        if(id && blockers[id]){
    	            blockers[id].hide();
    	        }
    	    }
    });
    var id_count = 0;
    var _uniqueId = function(){
        var id_base = "dojo_blocked",
            id;
        do{
            id = id_base + "_" + (++id_count);
        }while(d.byId(id));
        return id;
    };
})();