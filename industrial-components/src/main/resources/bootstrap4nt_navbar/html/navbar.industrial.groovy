import org.apache.taglibs.standard.functions.Functions
import org.jahia.services.content.JCRContentUtils
import org.jahia.services.render.RenderService
import org.jahia.services.render.Resource
import org.jahia.taglibs.jcr.node.JCRTagUtils
import org.slf4j.LoggerFactory

import javax.jcr.ItemNotFoundException

logger = LoggerFactory.getLogger(this.class)
logger.info("Hello Nav !")
Resource addResources =  new Resource(currentNode, "html", "industrial.addResources", currentResource.getContextConfiguration())
Resource basenavResource =  new Resource(currentNode, "html", "industrial.basenav", currentResource.getContextConfiguration())
Resource loginBtnResource =  new Resource(currentNode, "html", "hidden.login", currentResource.getContextConfiguration())

print RenderService.getInstance().render(addResources, renderContext)
def basenav = RenderService.getInstance().render(basenavResource, renderContext)
def loginBtn = RenderService.getInstance().render(loginBtnResource, renderContext)


//def response = renderContext.getResponse()
def addContainerFluid = currentNode.properties.addContainerWithinTheNavbar?
    currentNode.properties.addContainerWithinTheNavbar.boolean:false
getBrand={node ->
    def brand = [:]
    try{ brand.image = renderContext.getResponse().encodeURL(node.properties.brandImage.node.url) }
    catch (ItemNotFoundException e) { logger.warn("No brand image from : "+node.displayableName.toString()) }
    brand.name = node.properties.brandText.string

    return brand
}

def brand
switch (true){
    case JCRTagUtils.isNodeType(renderContext.site,'bootstrap4mix:siteBrand'):
        brand = getBrand(renderContext.site)
        break;
    case JCRTagUtils.isNodeType(currentNode,'bootstrap4mix:brand'):
        brand = getBrand(renderContext.site)
        break;
    default:
        brand=[:]
}

getBase={ ->
    curentPageNode = JCRTagUtils.isNodeType(renderContext.mainResource.node,'jmix:navMenuItem')?
            renderContext.mainResource.node :
            JCRTagUtils.getParentOfType(renderContext.mainResource.node,'jmix:navMenuItem')

    switch (currentNode.properties.root.string){
        case "currentPage": return curentPageNode
        case "parentPage":return curentPageNode.parent
        default:return renderContext.site.home
    }
}

def navHTML = '''
<nav class="${classes}">
    ${addContainerFluid?'<div class="container-fluid">':''}
'''
def a_brandHTML= '''
    <a class="navbar-brand" href="${baseUrl}">
        ${brand.image?'<img src="#" class="d-inline-block align-top" alt="">':''}
        ${brand.name}
    </a>'''
def btn_hamburgerHTML= '''
    <button class="${classes}" type="button"
        data-toggle="collapse" data-target="#navbar-${identifier}"
        aria-controls="navbar-${identifier}" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
'''
def form_searchHTML = '''
    <div class="navbar-nav ml-auto">
        <form method="post" class="search-form">
            <span class="icon ion ion-md-search"></span>
            <input type="text" class="form-control" placeholder="Search...">
        </form>
    </div>
'''
def nav = new groovy.text.StreamingTemplateEngine().createTemplate(navHTML)
def a_brand = new groovy.text.StreamingTemplateEngine().createTemplate(a_brandHTML)
def btn_hamburger = new groovy.text.StreamingTemplateEngine().createTemplate(btn_hamburgerHTML)
def form_search = new groovy.text.StreamingTemplateEngine().createTemplate(form_searchHTML)

print nav.make([
    classes: currentNode.properties.navClass?
        currentNode.properties.navClass.string : "navbar navbar-expand-lg navbar-light bg-light",
    addContainerFluid:addContainerFluid
])

print a_brand.make([
    baseUrl: renderContext.getResponse().encodeURL(getBase().url),
    brand:brand
])

print btn_hamburger.make([
    classes:currentNode.properties.buttonClass?
        currentNode.properties.buttonClass.string : "navbar-toggler navbar-toggler-right",
    identifier:currentNode.identifier
])
//TODO divClass
print "<div class=\"${divClass}\" id=\"navbar-${currentNode.identifier}\">"
//print basenav;
print form_search.make()
if(currentNode.properties.addLoginButton && currentNode.properties.addLoginButton.boolean)
    print loginBtn
print "</div>${addContainerFluid?'</div>':''}</nav>"


