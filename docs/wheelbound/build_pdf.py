"""Build a checkpoint PDF directly from the authoritative Markdown directory."""
import argparse
import hashlib
import re
from pathlib import Path
from xml.sax.saxutils import escape
from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import BaseDocTemplate, PageTemplate, Frame, Paragraph, Spacer, PageBreak, Table, TableStyle, KeepTogether
from reportlab.platypus.tableofcontents import TableOfContents

ROOT=Path(__file__).resolve().parent
ORDER=["README","BLUEPRINT","GAME_RULES","DECISIONS","PROGRESSION","WHEELS",
       "FATE_CARDS","FATE_SHOP","ECONOMY","BALANCE_CANDIDATE","GRAND_FATES","DEFY_FATE","PUNISHMENTS",
       "BOUNTIES","ACCOUNT_ACCESS","UI_UX","UX_RECOVERY_FLOWS","PERSISTENCE","ARCHITECTURE",
       "RUNELITE_INTEGRATION","POLICY_AND_FEASIBILITY","TECHNICAL_SPECIFICATIONS","CONTENT_CATALOGS","CATALOG_ACCEPTANCE","ACQUISITION_EVIDENCE","STATE_CONTRACT","EDGE_CASES","TESTING","SIMULATION_RESULTS",
       "IMPLEMENTATION_PLAN","DEVELOPMENT_PIPELINE","TRACEABILITY","SOURCES","OPEN_QUESTIONS","MONDAY_HANDOFF","FIRST_CODING_TASK","WORK_STATUS","TASKS"]
parser=argparse.ArgumentParser()
parser.add_argument("--commit",required=True)
parser.add_argument("--output",required=True)
args=parser.parse_args()
out=Path(args.output)
out.parent.mkdir(parents=True,exist_ok=True)
pdfmetrics.registerFont(TTFont("DejaVu","/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"))
pdfmetrics.registerFont(TTFont("DejaVuBold","/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"))
pdfmetrics.registerFontFamily("DejaVu",normal="DejaVu",bold="DejaVuBold",italic="DejaVu",boldItalic="DejaVuBold")
styles=getSampleStyleSheet()
styles.add(ParagraphStyle(name="WBBody",fontName="DejaVu",fontSize=8.8,leading=12.5,spaceAfter=6.5,splitLongWords=True))
styles.add(ParagraphStyle(name="WBH1",fontName="DejaVuBold",fontSize=21,leading=26,textColor=colors.HexColor("#352f27"),spaceAfter=14,keepWithNext=True))
styles.add(ParagraphStyle(name="WBH2",fontName="DejaVuBold",fontSize=12,leading=16,spaceBefore=12,spaceAfter=6,keepWithNext=True))
styles.add(ParagraphStyle(name="WBH3",fontName="DejaVuBold",fontSize=10,leading=14,spaceBefore=10,spaceAfter=5,keepWithNext=True))
styles.add(ParagraphStyle(name="WBCell",parent=styles["WBBody"],fontSize=7.6,leading=10.2,spaceAfter=0))
styles.add(ParagraphStyle(name="WBTOC",fontName="DejaVu",fontSize=9,leading=13,leftIndent=0,firstLineIndent=0,spaceBefore=0))
styles.add(ParagraphStyle(name="WBCover",fontName="DejaVuBold",fontSize=37,leading=43,textColor=colors.HexColor("#352f27")))

def markup(text):
    text=text.replace("→"," -> ").replace("—"," - ").replace("–","-")
    text=escape(text)
    text=re.sub(r"\[([^\]]+)\]\((https?://[^)]+)\)",r'<a href="\2" color="#6e572b">\1</a>',text)
    text=re.sub(r"\[([^\]]+)\]\(([^)]+)\)",r"\1 (\2)",text)
    text=re.sub(r"\*\*([^*]+)\*\*",r"<b>\1</b>",text)
    text=re.sub(r"`([^`]+)`",r"<font size='8'>\1</font>",text)
    return text

class Doc(BaseDocTemplate):
    def afterFlowable(self,flowable):
        if isinstance(flowable,Paragraph) and flowable.style.name=="WBH1" and getattr(flowable,"_toc_section",False):
            title=flowable.getPlainText()
            key="section"+str(self.page)
            self.canv.bookmarkPage(key)
            self.canv.addOutlineEntry(title,key,0)
            self.notify("TOCEntry",(0,title,self.page,key))

def footer(canvas,doc):
    width,height=A4
    canvas.saveState()
    canvas.setStrokeColor(colors.HexColor("#ba9c5c"));canvas.line(48,39,width-48,39)
    canvas.setFont("DejaVu",7)
    canvas.setFillColor(colors.HexColor("#655d50"))
    canvas.drawString(48,26,"Wheelbound | Design checkpoint | "+args.commit[:10])
    canvas.drawRightString(width-48,26,str(doc.page))
    canvas.restoreState()

doc=Doc(str(out),pagesize=A4,leftMargin=48,rightMargin=48,topMargin=48,bottomMargin=54,
        title="Wheelbound - Recovered Design, Research and Balance Checkpoint",
        author="Wheelbound design documentation")
doc.addPageTemplates(PageTemplate(id="main",frames=Frame(48,54,A4[0]-96,A4[1]-102,id="body",leftPadding=0,rightPadding=0,topPadding=0,bottomPadding=0),onPage=footer))
story=[Spacer(1,94),Paragraph("WHEELBOUND",styles["WBCover"]),Spacer(1,18),
       Paragraph("Recovered design, technical research<br/>and economy simulation checkpoint",styles["WBH1"]),Spacer(1,24),
       Paragraph("9 October 2026 | wheelbound-mode | Documentation only",styles["WBBody"]),
       Paragraph("Confirmed user rules, unverified historical details, new proposals and open decisions are explicitly distinguished. This is a checkpoint, not a final implementation-ready specification.",styles["WBBody"]),
       Paragraph("Executed models: 126,000 synthetic account trajectories across historical, corrected and sensitivity runs. Live RuneLite enforcement and Death's Coffer verification remain untested.",styles["WBBody"]),
       Spacer(1,22),Paragraph("Markdown source commit: "+args.commit,styles["WBBody"]),
       Paragraph("Repository: TheRealEddieDean/RunlitePLugin-Wheelbound<br/>Source: docs/wheelbound/",styles["WBBody"]),PageBreak()]
story.append(Paragraph("Contents",styles["WBH1"]))
toc=TableOfContents();toc.levelStyles=[styles["WBTOC"]]
story.extend([toc,PageBreak()])

for name in ORDER:
    text=(ROOT/(name+".md")).read_text()
    lines=text.splitlines()
    i=0
    while i<len(lines):
        line=lines[i].strip()
        if not line:
            i+=1;continue
        if name=="DECISIONS" and line.startswith("## WB-D"):
            group=[Paragraph(markup(line[3:]),styles["WBH2"])]
            i+=1
            while i<len(lines) and not lines[i].startswith("## "):
                field=lines[i].strip()
                if field:
                    group.append(Paragraph(markup(field[2:] if field.startswith("- ") else field),styles["WBBody"],bulletText="-" if field.startswith("- ") else None))
                i+=1
            story.append(KeepTogether(group))
            continue
        if line.startswith("|"):
            rows=[]
            while i<len(lines) and lines[i].strip().startswith("|"):
                cells=[c.strip() for c in lines[i].strip().strip("|").split("|")]
                if not all(re.fullmatch(r":?-+:?",c.replace(" ","")) for c in cells):
                    rows.append(cells)
                i+=1
            cols=max(map(len,rows))
            rows=[r+[""]*(cols-len(r)) for r in rows]
            table=Table([[Paragraph(markup(c),styles["WBCell"]) for c in r] for r in rows],
                        colWidths=[(A4[0]-96)/cols]*cols,repeatRows=1,hAlign="LEFT")
            table.setStyle(TableStyle([("BACKGROUND",(0,0),(-1,0),colors.HexColor("#e9dfc8")),
                ("VALIGN",(0,0),(-1,-1),"TOP"),("ROWBACKGROUNDS",(0,1),(-1,-1),[colors.white,colors.HexColor("#f6f3ec")]),
                ("BOTTOMPADDING",(0,0),(-1,-1),6),("TOPPADDING",(0,0),(-1,-1),6),
                ("LINEBELOW",(0,0),(-1,0),.6,colors.HexColor("#ba9c5c"))]))
            story.extend([table,Spacer(1,10)]);continue
        if line.startswith("# "):
            heading=Paragraph(markup(line[2:]),styles["WBH1"])
            heading._toc_section=True
            story.append(heading)
        elif line.startswith("## "):
            story.append(Paragraph(markup(line[3:]),styles["WBH2"]))
        elif line.startswith("### "):
            story.append(Paragraph(markup(line[4:]),styles["WBH3"]))
        elif line.startswith("- "):
            story.append(Paragraph(markup(line[2:]),styles["WBBody"],bulletText="-"))
        elif re.match(r"^\d+\. ",line):
            number,body=line.split(". ",1)
            story.append(Paragraph(markup(body),styles["WBBody"],bulletText=number+"."))
        elif line.startswith("`") and line.endswith("`"):
            story.append(Paragraph(markup(line),styles["WBBody"]))
        else:
            paragraph=[line]
            while i+1<len(lines) and lines[i+1].strip() and not lines[i+1].lstrip().startswith(("#","|","- ","`")) and not re.match(r"^\d+\. ",lines[i+1].strip()):
                i+=1;paragraph.append(lines[i].strip())
            story.append(Paragraph(markup(" ".join(paragraph)),styles["WBBody"]))
        i+=1
    if name!=ORDER[-1]: story.append(PageBreak())
doc.multiBuild(story)
print(str(out.resolve()))
