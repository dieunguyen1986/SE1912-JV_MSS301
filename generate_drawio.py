import xml.etree.ElementTree as ET

# Create root mxfile element
mxfile = ET.Element("mxfile", host="Electron", modified="2026-07-18T00:00:00.000Z", agent="5.0", version="21.0.0", type="device")
diagram = ET.SubElement(mxfile, "diagram", id="SagaFlowDiagram", name="TalentHub Saga Pattern Flow")
mxGraphModel = ET.SubElement(diagram, "mxGraphModel", dx="1200", dy="900", grid="1", gridSize="10", guides="1", tooltips="1", connect="1", arrows="1", fold="1", page="1", pageScale="1", pageWidth="1100", pageHeight="1050", math="0" shadow="0")
root = ET.SubElement(mxGraphModel, "root")

# Default cells
ET.SubElement(root, "mxCell", id="0")
ET.SubElement(root, "mxCell", id="1", parent="0")

def add_lifeline(id_name, label, x, width=100, height=940):
    style = "shape=umlLifeline;perimeter=lifelinePerimeter;whiteSpace=wrap;html=1;container=1;dropTarget=0;collapsible=0;recursiveResize=0;outlineConnect=0;portConstraint=eastwest;newEdgeStyle={};size=40;fillColor=#f5f5f5;strokeColor=#666666;fontStyle=1"
    cell = ET.SubElement(root, "mxCell", id=id_name, value=label, style=style, vertex="1", parent="1")
    ET.SubElement(cell, "mxGeometry", x=str(x), y="40", width=str(width), height=str(height), as="geometry")

# Add Lifelines
add_lifeline("candidate", "Candidate\n(Actor)", 60, 100)
add_lifeline("app_service", "Application\nService", 220, 120)
add_lifeline("rabbitmq", "RabbitMQ\nExchange", 420, 120)
add_lifeline("job_service", "Job\nService", 620, 120)
add_lifeline("cv_parser", "CV Parser\nService", 820, 120)
add_lifeline("notification", "Notification\nService", 1020, 120)

def add_message(id_name, source, target, y, label, is_return=False):
    style = "verticalAlign=bottom;endArrow=open;dashed=1;endSize=8;shadow=0;strokeWidth=1;" if is_return else "verticalAlign=bottom;endArrow=block;shadow=0;strokeWidth=1;"
    if "rabbitmq" in target or "rabbitmq" in source:
        style += "strokeColor=#FF8000;fontColor=#D35400;"
    
    cell = ET.SubElement(root, "mxCell", id=id_name, value=label, style=style, edge="1", parent="1", source=source, target=target)
    # Define geometry points
    geom = ET.SubElement(cell, "mxGeometry", relative="1", as="geometry")
    ET.SubElement(geom, "mxPoint", x="0", y=str(y), as="sourcePoint")
    ET.SubElement(geom, "mxPoint", x="0", y=str(y), as="targetPoint")

def add_self_message(id_name, source, y, label):
    style = "verticalAlign=bottom;endArrow=block;shadow=0;strokeWidth=1;edgeStyle=orthogonalEdgeStyle;curved=1;rounded=1;"
    cell = ET.SubElement(root, "mxCell", id=id_name, value=label, style=style, edge="1", parent="1", source=source, target=source)
    geom = ET.SubElement(cell, "mxGeometry", relative="1", as="geometry")
    ET.SubElement(geom, "mxPoint", x="0", y=str(y), as="sourcePoint")
    ET.SubElement(geom, "mxPoint", x="50", y=str(y+30), as="targetPoint")

def add_block_note(id_name, label, x, y, w, h, fillColor="#fff2cc", strokeColor="#d6b656"):
    style = f"shape=note;whiteSpace=wrap;html=1;size=12;fillColor={fillColor};strokeColor={strokeColor};spacingLeft=4;fontStyle=0;fontSize=11;"
    cell = ET.SubElement(root, "mxCell", id=id_name, value=label, style=style, vertex="1", parent="1")
    ET.SubElement(cell, "mxGeometry", x=str(x), y=str(y), width=str(w), height=str(h), as="geometry")

# Step 1: Submit Application
add_message("msg1", "candidate", "app_service", 120, "POST /api/v1/applications")
add_self_message("msg2", "app_service", 140, "Save App PENDING\n& Outbox Event")
add_message("msg3", "app_service", "candidate", 190, "201 Created (applicationId)", is_return=True)

# Step 2: Outbox relay
add_self_message("msg4", "app_service", 220, "Outbox Scheduler\npolls & processes")
add_message("msg5", "app_service", "rabbitmq", 260, "Publish JobSlotReservedEvent\n(RK: job.applied-increment)")
add_message("msg6", "rabbitmq", "job_service", 290, "job.application-applied Queue\n(JobSlotReservedEvent)")

# Step 3: Job Service logic
add_self_message("msg7", "job_service", 310, "Check capacity &\nincrement applicantCount")

# Alternate Path A: Happy Path (Slots OK)
add_block_note("note_happy", "<b>[HAPPY PATH - Capacity OK]</b>", 500, 360, 200, 30, fillColor="#d5e8d4", strokeColor="#82b366")

add_message("msg8", "job_service", "rabbitmq", 410, "Publish JobSlotReservedEvent\n(RK: job.slot.reserved)")

# Both Notification and CV Parser receive
add_message("msg9", "rabbitmq", "cv_parser", 440, "cv.job.slot.reserved.queue\n(JobSlotReservedEvent)")
add_message("msg10", "rabbitmq", "notification", 460, "notification.job.slot.reserved.queue\n(JobSlotReservedEvent)")
add_self_message("msg11", "notification", 480, "sendAutoReply() email")

# CV Parser result options:
add_block_note("note_cv_ok", "<b>[CV Parsing Success]</b>", 740, 520, 160, 35, fillColor="#d5e8d4", strokeColor="#82b366")
add_message("msg12", "cv_parser", "rabbitmq", 570, "Publish CVParsedEvent\n(RK: cv.parsed.success)")
add_message("msg13", "rabbitmq", "app_service", 600, "application.cv.parsed.success.queue\n(CVParsedEvent)")
add_self_message("msg14", "app_service", 620, "Update Application\nto CV_SCREENING")
add_message("msg15", "rabbitmq", "notification", 650, "notification.cv-parse-success\n(CVParsedEvent)")
add_self_message("msg16", "notification", 670, "sendCvSuccessNotification() email")

add_block_note("note_cv_fail", "<b>[CV Parsing Failed - Compensation]</b>", 740, 710, 220, 35, fillColor="#f8cecc", strokeColor="#b85450")
add_message("msg17", "cv_parser", "rabbitmq", 760, "Publish CVParseFailedEvent\n(RK: cv.parsed.failed)")
# CV Parser fail notifies App Service, Job Service, Notification
add_message("msg18", "rabbitmq", "app_service", 790, "application.cv.parsed.failed.queue")
add_self_message("msg19", "app_service", 810, "Compensation: Update\nstatus to REJECTED")

add_message("msg20", "rabbitmq", "job_service", 830, "job.cv.parsed.failed.queue")
add_self_message("msg21", "job_service", 850, "Compensation:\ndecrement applicantCount")

add_message("msg22", "rabbitmq", "notification", 875, "notification.cv-parse-failed")
add_self_message("msg23", "notification", 895, "sendCvNotMatchNotification() email")

# Alternate Path B: Slots Full (Job Reject)
add_block_note("note_reject", "<b>[SLOTS FULL / JOB CLOSED]</b>", 480, 930, 220, 30, fillColor="#f8cecc", strokeColor="#b85450")
add_message("msg24", "job_service", "rabbitmq", 970, "Publish JobSlotRejectedEvent\n(RK: job.slot.rejected)")
add_message("msg25", "rabbitmq", "app_service", 1000, "application.job.slot.rejected.queue")
add_self_message("msg26", "app_service", 1020, "Compensation: Update\nstatus to REJECTED")
add_message("msg27", "rabbitmq", "notification", 1045, "notification.slot-rejected")
add_self_message("msg28", "notification", 1065, "sendSlotFullRejection() email")

# Write to file
tree = ET.ElementTree(mxfile)
ET.indent(tree, space="  ", level=0)
tree.write("/Users/dieunt/Documents/FPTU/1_Training/SU26/SE1912-JV/Projects/talenthub_se1912/logs/saga_flow.drawio", encoding="utf-8", xml_declaration=True)
print("Drawio XML generated successfully!")
