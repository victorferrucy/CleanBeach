package fi.pss.cleanbeach.ui.views.events;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.vaadin.addon.touchkit.extensions.TouchKitIcon;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

import fi.pss.cleanbeach.data.Image;
import fi.pss.cleanbeach.ui.MyTouchKitUI;

public class CommentInputLayout extends NavigationView {

	private static final long serialVersionUID = 5640950674463792976L;
	private final Upload addImage;

	private Image uploadedImage;

	public CommentInputLayout(final fi.pss.cleanbeach.data.Event e,
			boolean addImageImmediately, final EventsPresenter presenter) {

		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		root.setSizeFull();
		addStyleName("addcomment");

		setContent(root);
		if (addImageImmediately) {
			setCaption("Add image");
		} else {
			setCaption("Add comment");
		}

		final TextArea comment = new TextArea();
		comment.setSizeFull();
		root.addComponent(comment);
		root.setExpandRatio(comment, 1);

		addImage = new Upload();
		addImage.setButtonCaption("Add Image to comment");
		addImage.setImmediate(true);
		addImage.setWidth("100%");
		ImageUploader ul = new ImageUploader(addImage);
		addImage.setReceiver(ul);
		addImage.addSucceededListener(ul);
		TouchKitIcon.cameraRetro.addTo(addImage);
		root.addComponent(addImage);

		Button saveComment = new Button("Add comment");
		TouchKitIcon.comment.addTo(saveComment);
		saveComment.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 5577262959129927178L;

			@Override
			public void buttonClick(ClickEvent event) {
				presenter.addComment(e, comment.getValue(), uploadedImage,
						MyTouchKitUI.getCurrentUser());
			}
		});
		root.addComponent(saveComment);

		if (addImageImmediately) {
			openUpload();
		}
	}

	protected void openUpload() {
		// TODO how?
	}

	// Implement both receiver that saves upload in a file and
	// listener for successful upload
	class ImageUploader implements Receiver, SucceededListener,
			ProgressListener {
		private static final long serialVersionUID = 1L;

		final static int maxLength = 10000000;
		ByteArrayOutputStream fos = null;
		String filename;
		Upload upload;

		public ImageUploader(Upload upload) {
			this.upload = upload;
		}

		@Override
		public OutputStream receiveUpload(String filename, String mimeType) {
			this.filename = filename;
			fos = new ByteArrayOutputStream(maxLength + 1);
			return fos; // Return the output stream to write to
		}

		@Override
		public void updateProgress(long readBytes, long contentLength) {
			if (readBytes > maxLength) {
				Notification.show("Too big content");
				upload.interruptUpload();
			}
		}

		@Override
		public void uploadSucceeded(SucceededEvent event) {
			uploadedImage = new Image();
			uploadedImage.setContent(fos.toByteArray());
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			uploadedImage.setMimetype("image/jpeg");
			uploadedImage.setUploaded(new Date());
		}
	};
}