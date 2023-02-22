const router = require("express").Router();
const User = require("../Models/User");
const Post = require("../Models/Post");
const CryptoJS = require("crypto-js");

router.get("/getimages", async (req, res) => {
  try {
    const posts = await Post.find();

    let images = [];
    posts.forEach((post) => {
      images = images.concat(post.images);
    });

    res.status(200).send(images);
  } catch (error) {
    res.status(500).json(error);
  }
});

router.post("/change-password", async (req, res) => {
  try {
    // Find the user by their username
    const user = await User.findOne({ username: req.body.username });
    if (!user) return res.status(404).json("User not found");

    // Check if the current password is correct
    const bytes = CryptoJS.AES.decrypt(
      user.password,
      process.env.PASSWORD_SECRET
    );
    const originalPassword = bytes.toString(CryptoJS.enc.Utf8);
    if (originalPassword !== req.body.currentPassword)
      return res.status(401).json("Incorrect current password");

    // Update the user's password
    const newPassword = req.body.newPassword;
    const encryptedPassword = CryptoJS.AES.encrypt(
      newPassword,
      process.env.PASSWORD_SECRET
    );
    user.password = encryptedPassword;
    await user.save();

    res.status(200).json("Password changed successfully");
  } catch (error) {
    res.status(500).json(error);
    console.log(error);
  }
});

module.exports = router;
